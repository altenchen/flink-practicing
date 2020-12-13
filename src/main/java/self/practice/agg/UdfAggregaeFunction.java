package self.practice.agg;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import self.practice.func.AverageAggregate;
import self.practice.config.KafkaClient;
import java.util.Map;

/**
 * @author altenchen
 * @time 2020/12/12
 * @description 功能
 */
@Slf4j
public class UdfAggregaeFunction {
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(20, 5000));
        
        DataStreamSource<String> kafkaSource = env.addSource(KafkaClient.getKafkaConsumer(), "kafka_source");

        SingleOutputStreamOperator<Tuple2<String, Integer>> mapStream = kafkaSource
                .flatMap(new TransFlatMap());

        SingleOutputStreamOperator<Tuple2<String, Double>> aggregate = mapStream.keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .aggregate(new AverageAggregate());
        
        aggregate.printToErr();
        
        env.execute("AverageAggregate");
    }


    static class TransFlatMap implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            Map<String, Integer> hashMap = JSONUtil.toBean(value, Map.class);
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                log.info("consumed records: key = [{}], record = [{}]", entry.getKey(), entry.getValue());
                out.collect(new Tuple2<String, Integer>(entry.getKey(), entry.getValue()));
            }
        }
    }
  
    
}
