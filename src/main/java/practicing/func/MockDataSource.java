package practicing.func;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import java.util.HashMap;
import java.util.Random;

/**
 * @description:
 * @create: 2020/12/13
 * @author: altenchen
 */
public class MockDataSource implements SourceFunction<String>{

    private boolean isRunning = true;

    @Override
    public void run(SourceContext<String> sourceContext) throws Exception {
        Random random = new Random();
        while (isRunning) {
            HashMap<String, Integer> scores = new HashMap<>();
            scores.put("Kobe", 100);
            scores.put("Jordan", 50);
            scores.put("James", 20);
            scores.put("Allen", 60);
            scores.put("Yaoming", 10);

            JSONObject jsonObject = JSONUtil.parseFromMap(scores);
            sourceContext.collect(jsonObject.toString());
            Thread.sleep(2000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
