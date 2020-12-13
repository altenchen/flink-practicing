package self.practice.mock;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import self.practice.config.KafkaClient;
import self.practice.func.MockDataSource;

/**
 * @description:
 * @create: 2020/12/13
 * @author: altenchen
 */
public class KafkaProducer {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.enableCheckpointing(5000);

        DataStreamSource<String> text = env.addSource(new MockDataSource()).setParallelism(1);

        FlinkKafkaProducer<String> kafkaProducer = KafkaClient.getKafkaProducer();

        kafkaProducer.setWriteTimestampToKafka(true);
        text.addSink(kafkaProducer);

        env.execute();
    }

}
