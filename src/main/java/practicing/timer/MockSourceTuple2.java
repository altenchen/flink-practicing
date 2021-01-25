package practicing.timer;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author altenchen
 * @time 2021/1/25
 * @description 功能
 */
public class MockSourceTuple2 implements SourceFunction<Tuple2<String, Long>> {
    
    
    private volatile boolean isRunning = true;
    
    private static List<String> names = new ArrayList<>();
    static {
        names.add("Kobe");
        names.add("James");
        names.add("Jordan");
        names.add("Paulo");
    }
    
    private static Random random = new Random();
    
    private static long number = 1;
    
    @Override
    public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
        while (isRunning) {
            int index = random.nextInt(4);
            ctx.collect(Tuple2.of(names.get(index), number));
            number += 1;
            Thread.sleep(1000);
        }
    }
    
    @Override
    public void cancel() {
        isRunning = false;
    }
}
