package self.practice.config;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import self.practice.util.PropertiesUtil;

import java.util.Properties;

/**
 * @author altenchen
 * @create 功能
 */
public class KafkaClient {

    public static Properties getProperties(){
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", PropertiesUtil.getStringValue("kafka.bootstrap.servers"));
        properties.setProperty("group.id", PropertiesUtil.getStringValue("kafka.group.id"));
        return properties;
    }
    
    public static FlinkKafkaConsumer<String> getKafkaConsumer(){
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer(
                PropertiesUtil.getStringValue("kafka.source.topic"),
                new SimpleStringSchema(),
                getProperties());
        
        setConsumingMode(consumer);
    
        return consumer;
    }
    
    private static void setConsumingMode(FlinkKafkaConsumer<String> consumer) {
        if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.group.offset"))) {
            //从kafka消费组的offset开始消费
            consumer.setStartFromGroupOffsets();
        } else if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.latest.offset"))) {
            //从kafka最新的offset开始消费
            consumer.setStartFromLatest();
        } else if ("true".equals(PropertiesUtil.getStringValue("kafka.enable.consume.from.commit.checkpoint.offset"))) {
            //从kafka提交在checkpoint的offset开始消费
            consumer.setCommitOffsetsOnCheckpoints(true);
        } else {
            consumer.setStartFromLatest();
        }
    }
    
    /**
     * 获取生产者实例
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer() {
        FlinkKafkaProducer myProducer = new FlinkKafkaProducer<>(PropertiesUtil.getStringValue("kafka.bootstrap.servers"), PropertiesUtil.getStringValue("kafka.source.topic"), new SimpleStringSchema());
        return myProducer;
    }

}
