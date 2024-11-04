package simpleTest;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName:FlinkEnvBuilder
 * @Auther: krz
 * @Date: 2022/8/4 14:52
 * @Version: v1.0
 * @DESC
 */
public class FlinkEnvBuilder {

    private static Config config = ConfigFactory.load();

    public static StreamExecutionEnvironment initEnv(){

        if (config.getString("flink.running.mode").equals("local")) {
            Configuration conf = new Configuration();
            conf.setString(RestOptions.BIND_PORT, "8081-8089");
            conf.setBoolean("classloader.check-leaked-classloader", false);
            return StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        } else {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setParallelism(config.getInt("flink.global.parallelism"));
            env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
            env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
            env.setStateBackend(new FsStateBackend(config.getString("flink.checkpoint.state.dir")));
            env.enableCheckpointing(config.getLong("flink.checkpoint.interval.ms"), CheckpointingMode.EXACTLY_ONCE);
            env.getCheckpointConfig().setCheckpointTimeout(config.getLong("flink.checkpoint.timeout.ms"));
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(config.getInt("flink.restart.attempts"), Time.of(10, TimeUnit.SECONDS)));
            return env;
        }

    }


    /**
     * 获取执行入口
     *
     * @return 执行入口
     */
    private static StreamExecutionEnvironment getExecuteEnv() {
        if (config.getString("flink.running.mode").equals("local")) {
            Configuration conf = new Configuration();
            conf.setString(RestOptions.BIND_PORT, "8081-8089");
            conf.setBoolean("classloader.check-leaked-classloader", false);
            return StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        } else {
            return StreamExecutionEnvironment.getExecutionEnvironment();
        }
    }

    /**
     * 设置任务运行参数
     *
     * @param env
     */
    public static void setEnvParams(StreamExecutionEnvironment env) {
        env.setParallelism(config.getInt("flink.global.parallelism"));
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        setCheckpointParams(env);
//        setSavepointParams(env);

        env.setRestartStrategy(
                RestartStrategies.fixedDelayRestart(
                        config.getInt("flink.restart.attempts"),
                        Time.of(config.getInt("flink.restart.delayInterval.second"), TimeUnit.SECONDS)
                )
        );
    }

    private static void setCheckpointParams(StreamExecutionEnvironment env) {
        // start a checkpoint every n ms
        env.enableCheckpointing(config.getLong("flink.checkpoint.interval.ms"));

        // advanced options:
        // set mode to exactly-once (this is the default)
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // make sure 500 ms of progress happen between checkpoints
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(config.getLong("flink.checkpoint.pause.between.ms"));
        // checkpoints have to complete within one minute, or are discarded
        env.getCheckpointConfig().setCheckpointTimeout(config.getLong("flink.checkpoint.timeout.ms"));
        // only two consecutive checkpoint failures are tolerated
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(config.getInt("flink.checkpoint.failure.tolerance.number"));
        // allow only one checkpoint to be in progress at the same time
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(config.getInt("flink.checkpoint.concurrent.number"));
        // enable externalized checkpoints which are retained after job cancellation
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        // enables the experimental unaligned checkpoints
        if (config.getString("flink.checkpoint.enable.unaligned").equals("true")) {
            env.getCheckpointConfig().enableUnalignedCheckpoints();
        }
        // sets the checkpoint storage where checkpoint snapshots will be written
        env.setStateBackend(new FsStateBackend(config.getString("flink.checkpoint.state.dir")));

    }

}

