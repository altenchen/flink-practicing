package self.practice.func;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;

/**
 * @author altenchen
 * @time 2020/12/12
 * @description 功能
 */
public class AverageAggregate implements AggregateFunction<Tuple2<String, Integer>, Tuple2<Integer, Integer>, Double> {
    
    @Override
    public Tuple2<Integer, Integer> createAccumulator() {
        return new Tuple2<>(0, 0);
    }
    
    @Override
    public Tuple2<Integer, Integer> add(Tuple2<String, Integer> value, Tuple2<Integer, Integer> acc) {
        return new Tuple2<>(acc.f0 + value.f1, acc.f1 + 1);
    }
    
    @Override
    public Double getResult(Tuple2<Integer, Integer> acc) {
        return Double.valueOf(acc.f0 / acc.f1);
    }
    
    @Override
    public Tuple2<Integer, Integer> merge(Tuple2<Integer, Integer> acc1, Tuple2<Integer, Integer> acc2) {
        return new Tuple2<>(acc1.f0 + acc2.f0, acc2.f1 + acc2.f1);
    }
}
