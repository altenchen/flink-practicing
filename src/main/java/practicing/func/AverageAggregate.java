package practicing.func;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * @author altenchen
 * @time 2020/12/12
 * @description 功能
 */
public class AverageAggregate implements AggregateFunction<Tuple2<String, Integer>, Tuple3<Integer, Integer, String>, Tuple2<String, Double>> {
    
    @Override
    public Tuple3<Integer, Integer, String> createAccumulator() {
        return new Tuple3<>(0, 0, "");
    }
    
    @Override
    public Tuple3<Integer, Integer, String> add(Tuple2<String, Integer> value, Tuple3<Integer, Integer, String> acc) {
        return new Tuple3<>(acc.f0 + value.f1, acc.f1 + 1, value.f0);
    }
    
    @Override
    public Tuple2<String, Double> getResult(Tuple3<Integer, Integer, String> acc) {
        return new Tuple2<>(acc.f2, Double.valueOf(acc.f0 / acc.f1));
    }
    
    @Override
    public Tuple3<Integer, Integer, String> merge(Tuple3<Integer, Integer, String> acc1, Tuple3<Integer, Integer, String> acc2) {
        return new Tuple3<>(acc1.f0 + acc2.f0, acc2.f1 + acc2.f1, acc1.f2);
    }
}
