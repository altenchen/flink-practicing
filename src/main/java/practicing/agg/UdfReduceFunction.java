package practicing.agg;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author altenchen
 * @time 2020/12/12
 * @description 功能
 */
@Slf4j
public class UdfReduceFunction {
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple2<String, Integer>> input = env.fromElements(scores);
        
        SingleOutputStreamOperator<Tuple2<String, Integer>> total = input.keyBy(0)
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> tuple1, Tuple2<String, Integer> tuple2) throws Exception {
                        log.info("key=[{}], tuple1=[{}], tuple2=[{}]", tuple1.f0, tuple1.f1, tuple2.f1);
                        return new Tuple2<>(tuple1.f0, tuple1.f1 + tuple2.f1);
                    }
                });
    
        total.printToErr();
        
        env.execute("ReduceFunction");
    }
    
    public static final Tuple2[] scores = new Tuple2[]{
            Tuple2.of("Kobe", 32),
            Tuple2.of("JORDAN", 12),
            Tuple2.of("JAMES", 22),
            Tuple2.of("Kobe", 32),
            Tuple2.of("AVERSION", 22),
            Tuple2.of("Kobe", 112),
            Tuple2.of("Kobe", 113),
            Tuple2.of("Kobe", 114),
            Tuple2.of("JAMES", 62),
            Tuple2.of("JORDAN", 23)
    };


}
