package self.practice.agg;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import self.practice.func.AverageAggregate;
import self.practice.kafkaSource.config.KafkaClient;

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
                .map(record ->
                {
                    String[] split = record.split(",");
                    String key = split[0];
                    String value = split[1];
                    log.info("consumed records: key = [{}], record = [{}]", key, value);
                    return new Tuple2<>(key, Integer.valueOf(value));
                })
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                    @Override
                    public TypeInformation<Tuple2<String, Integer>> getTypeInfo() {
                        return super.getTypeInfo();
                    }
                });
    
        SingleOutputStreamOperator<Tuple2<String, Double>> aggregate = mapStream.keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .aggregate(new AverageAggregate());
        
        aggregate.printToErr();
        
        env.execute("AverageAggregate");
    }
  
    
}
