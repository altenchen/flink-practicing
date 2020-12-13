package self.practice.topN;

import cn.hutool.json.JSONUtil;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;
import self.practice.kafkaSource.config.KafkaClient;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description:
 * @create: 2020/12/13
 * @author: altenchen
 */
public class TopNCalculator {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.enableCheckpointing(60 * 1000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(30 * 1000);

        DataStreamSource<String> stream = env.addSource(KafkaClient.getKafkaConsumer(), "kafka-source");

        SingleOutputStreamOperator<OrderDetail> orderStream = stream.map(msg -> JSONUtil.toBean(msg, OrderDetail.class));

        SingleOutputStreamOperator<OrderDetail> dataStream = orderStream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<OrderDetail>() {
            private long currentTimeStamp = 0L;
            private long maxOutOfOrderness = 5000L;

            @Override
            public long extractTimestamp(OrderDetail orderDetail, long l) {
                return orderDetail.getTimstamp();
            }

            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return new Watermark(currentTimeStamp - maxOutOfOrderness);
            }
        });

        SingleOutputStreamOperator<OrderDetail> reduce = dataStream
                .keyBy((KeySelector<OrderDetail, Object>) value -> value.getUserId())
                .window(SlidingProcessingTimeWindows.of(Time.seconds(600), Time.seconds(20)))
                .reduce(new ReduceFunction<OrderDetail>() {
                    @Override
                    public OrderDetail reduce(OrderDetail value1, OrderDetail value2) throws Exception {
                        return new OrderDetail(
                                value1.getUserId(), value1.getItemId(), value1.getCityName(), value1.getPrice() + value2.getPrice(), value1.getTimstamp()
                        );
                    }
                });

        SingleOutputStreamOperator<Tuple2<Double, OrderDetail>> process = reduce.windowAll(TumblingEventTimeWindows.of(Time.seconds(20)))
                .process(new ProcessAllWindowFunction<OrderDetail, Tuple2<Double, OrderDetail>, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<OrderDetail> elements, Collector<Tuple2<Double, OrderDetail>> out) throws Exception {
                        TreeMap<Double, OrderDetail> treeMap = new TreeMap<>(new Comparator<Double>() {
                            @Override
                            public int compare(Double o1, Double o2) {
                                return o1 < o2 ? -1 : 1;
                            }
                        });
                        Iterator<OrderDetail> iterator = elements.iterator();
                        if (iterator.hasNext()) {
                            treeMap.put(iterator.next().getPrice(), iterator.next());
                            if (treeMap.size() > 10) {
                                treeMap.pollLastEntry();
                            }
                        }

                        for (Map.Entry<Double, OrderDetail> entry : treeMap.entrySet()) {
                            out.collect(Tuple2.of(entry.getKey(), entry.getValue()));
                        }
                    }
                });

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").setPort(6379).build();
        process.addSink(new RedisSink<>(conf, new RedisMapper<Tuple2<Double, OrderDetail>>() {
            private final String TOPN_PREFIX = "TOPN:";
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, TOPN_PREFIX);
            }

            @Override
            public String getKeyFromData(Tuple2<Double, OrderDetail> data) {
                return String.valueOf(data.f0);
            }

            @Override
            public String getValueFromData(Tuple2<Double, OrderDetail> data) {
                return String.valueOf(data.f1.toString());
            }
        }));


    }


}
