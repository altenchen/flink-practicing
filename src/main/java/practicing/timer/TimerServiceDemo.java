package practicing.timer;

import cn.hutool.core.date.DateUtil;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author altenchen
 * @time 2021/1/25
 * @description 功能
 */
public class TimerServiceDemo {
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        
        env.addSource(new MockSourceTuple2())
                .keyBy((KeySelector<Tuple2<String, Long>, String>) soutceStream -> soutceStream.getField(0))
                .process(new KeyedProcessFunction<String, Tuple2<String, Long>, String>() {
                    
                    private Map<String, List<Long>> cache = new ConcurrentHashMap<>();
                    private boolean first = true;
    
                    @Override
                    public void processElement(Tuple2<String, Long> value, Context ctx, Collector<String> out) throws Exception {
                        if (first) {
                            first = false;
                            long time = System.currentTimeMillis();
                            System.out.println("定时器第一次注册：" + DateUtil.date(time));
                            ctx.timerService().registerProcessingTimeTimer(time + 5000);
                        }
                        if (cache.containsKey(value.f0)) {
                            List<Long> longs = cache.get(value.f0);
                            longs.add(value.f1);
                        } else {
                            List<Long> longs = new ArrayList<>();
                            longs.add(value.f1);
                            cache.put(value.f0, longs);
                        }
                    }
    
                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        System.out.println("定时器触发：" + DateUtil.date(timestamp));
                        StringBuilder builder = new StringBuilder();
                        for (Map.Entry<String, List<Long>> entry : cache.entrySet()) {
                            builder.append(entry.getKey()).append(":");
                            for (long value : entry.getValue()) {
                                builder.append(value).append(",");
                            }
                            builder.delete(builder.length() - 1, builder.length()).append(";");
                            cache.remove(entry.getKey());
                        }
                        System.out.println("定时器注册");
                        ctx.timerService().registerProcessingTimeTimer(timestamp + 5000);
                        out.collect(builder.toString());
                    }
                })
                .print("处理结果：");
                
        env.execute();
    
    }
    
    
    public static class Descriptors {
        public static final MapStateDescriptor<String, List> cache =
                new MapStateDescriptor<String, List>(
                        "cache",
                        BasicTypeInfo.STRING_TYPE_INFO,
                        TypeInformation.of(List.class)
                );
    }
    
}
