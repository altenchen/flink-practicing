package practicing.etl;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import practicing.config.KafkaClient;
import practicing.util.PropertiesUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author altenchen
 * @time 2020/12/17
 * @description 功能
 */
public class StreamingETLJob {
    
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        env.enableCheckpointing(TimeUnit.MINUTES.toMinutes(1));
        
        DataStreamSource<String> stream = env.addSource(KafkaClient.getKafkaConsumer(), "kafka-source");
        
        StreamingFileSink<String> sink = StreamingFileSink
                .forRowFormat(new Path(PropertiesUtil.getStringValue("hdfs.sink.path")), new SimpleStringEncoder<String>())
                .withBucketAssigner(new EventTimeBucketAssigner())
                .build();
        
        stream.printToErr();
        
        stream.addSink(sink);
        
        env.execute("StreamingETLJob");
    }
    
    
}
