package self.practice.agg;

import org.apache.flink.api.common.functions.AggregateFunction;
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
public class UdfAggregaeFunction {
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        DataStreamSource<String> kafkaSource = env.addSource(KafkaClient.getKafkaConsumer(), "kafka_source");
    
        SingleOutputStreamOperator<Tuple2<String, Integer>> mapStream = kafkaSource
                .map(record -> new Tuple2<>(record.split(",")[0], Integer.valueOf(record.split(",")[1])))
                .returns(new TypeHint<Tuple2<String, Integer>>() {
                    @Override
                    public TypeInformation<Tuple2<String, Integer>> getTypeInfo() {
                        return super.getTypeInfo();
                    }
                });
    
        SingleOutputStreamOperator<Double> aggregate = mapStream.keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .aggregate(new AverageAggregate());
        
        aggregate.printToErr();
        
        env.execute("AverageAggregate");
    }
    
    
    
    public static final Tuple2[] scores = new Tuple2[]{
            Tuple2.of("Kobe", 32),
            Tuple2.of("JORDAN", 12),
            Tuple2.of("JAMES", 22),
            Tuple2.of("Kobe", 32),
            Tuple2.of("AVERSION", 22),
            Tuple2.of("Kobe", 112),
            Tuple2.of("JAMES", 62),
            Tuple2.of("JORDAN", 23)
    };
    
}
