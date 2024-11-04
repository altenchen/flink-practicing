package simpleTest;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author: altenchen
 * @time: 2022/7/11
 * @description: kafka客户端
 */
@Slf4j
public class KafkaUtil {

    private static final String COMMA_SEPARATOR = ",";

    private static final Config config = ConfigFactory.load();


    public static FlinkKafkaConsumer<String> getCustomKafkaConsumer(String topicList, String bootstrapServer, boolean isSasl) throws Exception {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getString("kafka.group.id"));
        properties.put(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, config.getString("kafka.partition-discovery.interval-millis"));

        if (config.getString("kafka.enable.default.offset").equals("true")) {
            properties.put("auto.offset.reset", config.getString("kafka.default.offset.type"));
        }

        if (isSasl) {
            properties.setProperty("security.protocol", "SASL_PLAINTEXT");
            properties.setProperty("sasl.mechanism", "SCRAM-SHA-512");
            properties.setProperty("sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + config.getString("sasl.jaas.config.username") + "\" password=\""+ config.getString("sasl.jaas.config.password") +"\";"
            );
        }

        FlinkKafkaConsumer<String> consumer= new FlinkKafkaConsumer<>(
                topicList,
                new SimpleStringSchema(),
                properties
        );

        setStartupMode(consumer, topicList, bootstrapServer, isSasl);

        return consumer;
    }

    /**
     * 设置消费策略
     *
     * @param consumer
     * @param targetTopic
     * @param bootstrap
     * @param isSasl
     * @param <T>
     */
    private static <T extends FlinkKafkaConsumer> void setStartupMode(T consumer, String targetTopic, String bootstrap, boolean isSasl) throws Exception {
        String startupMode = config.getString("kafka.offset.start");
        switch (startupMode) {
            case "earliest":
                log.info("Kafka消费者的消费策略为：{}", startupMode);
                consumer.setStartFromEarliest();
                break;
            case "latest":
                log.info("Kafka消费者的消费策略为：{}", startupMode);
                consumer.setStartFromLatest();
                break;
            case "group_offsets": //如果消费组不存在，会按照默认消费策略进行消费
                log.info("Kafka消费者的消费策略为：{}", startupMode);
                consumer.setCommitOffsetsOnCheckpoints(true);
                consumer.setStartFromGroupOffsets();
                break;
            case "specific_timestamp":
                log.info("Kafka消费者的消费策略为：{}, 起始消费时间戳为：{}",
                        startupMode,
                        config.getString("kafka.start.from.timestamp")
                );
                consumer.setStartFromTimestamp(config.getLong("kafka.start.from.timestamp"));
                break;
            case "custom_group_offset": //如果消费组不存在，按照earliest策略消费
//                setCustomGroupOffsetMode(consumer, targetTopic, bootstrap, isSasl, startupMode);
                break;
            default:
                throw new Exception(startupMode + " mode is not supported.");
        }
    }


    private static OffsetResetStrategy getConsumerOffset(String offsetMode) {
        switch (offsetMode) {
            case "earliest":
                return OffsetResetStrategy.EARLIEST;
            case "latest":
                return OffsetResetStrategy.LATEST;
            default:
                log.error(offsetMode + " mode is not supported, use default none strategy");
                return OffsetResetStrategy.NONE;
        }
    }

    public static FlinkKafkaConsumer<String> getKafkaConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrap.source.servers"));
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getString("kafka.group.id"));

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(config.getString("kafka.source.topic"), new SimpleStringSchema(), properties);

        setStartupMode(consumer);

        return consumer;
    }


    public static FlinkKafkaConsumer<String> getKafkaConsumer(String topic) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrap.servers"));
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getString("kafka.group.id"));

        properties.put(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, config.getString("kafka.partition-discovery.interval-millis"));

        //设置默认消费组
        if (config.getString("kafka.enable.default.offset").equals("true")) {
            properties.put("auto.offset.reset", config.getString("kafka.default.offset.type"));
        }

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), properties);

        setStartupMode(consumer);

        return consumer;
    }



    /**
     * 设置消费策略
     *
     * @param consumer
     * @param <T>
     */
    private static <T extends FlinkKafkaConsumer> void setStartupMode(T consumer) {
        String startupMode = config.getString("kafka.offset.start");
        switch (startupMode) {
            case "earliest":
                consumer.setStartFromEarliest();
                break;
            case "latest":
                consumer.setStartFromLatest();
                break;
            case "group_offsets":
                consumer.setStartFromGroupOffsets();
                break;
            case "commit_offset_on_cp":
                consumer.setCommitOffsetsOnCheckpoints(true);
                break;
            default:
                log.error(startupMode + " mode is not supported.");
                break;
        }
    }

    /**
     * 获取生产者实例
     *
     * @param sinkTopic
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer(String sinkTopic) {

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", config.getString("kafka.bootstrap.servers"));
        properties.setProperty("log.flush.interval.messages", config.getString("log.flush.interval.messages"));
        properties.setProperty("log.flush.interval.ms", config.getString("log.flush.interval.ms"));
        properties.setProperty("max.request.size", config.getString("kafka.producer.max.request.size"));

        return new FlinkKafkaProducer<>(sinkTopic, new SimpleStringSchema(), properties);
    }


    private static Map<String, String> buildDidTopicMapping(String sinkTopics) {
        String[] split = sinkTopics.split(",");
        Map<String, String> res = new HashMap<>();
        for (String elem : split) {
            String[] topics = elem.split("_");
            res.put(topics[0], topics[1]);
        }
        return res;
    }


    /**
     * 获取生产者实例
     *
     * @param sinkTopic
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer(String sinkTopic, String kafkaCluster) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", config.getString("kafka.bootstrap.source.servers"));
        properties.setProperty("log.flush.interval.messages", config.getString("log.flush.interval.messages"));
        properties.setProperty("log.flush.interval.ms", config.getString("log.flush.interval.ms"));

        if (kafkaCluster.equals("not_cloud")) {
            return new FlinkKafkaProducer<>(sinkTopic, new SimpleStringSchema(), properties);
        } else {
            properties.setProperty("security.protocol", "SASL_PLAINTEXT");
            properties.put("sasl.mechanism", "SCRAM-SHA-512");

            if (kafkaCluster.equals("baidu_cloud_dev")) {
                properties.put("sasl.jaas.config",
                        "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + config.getString("sasl.jaas.config.username") + "\" password=\""+ config.getString("sasl.jaas.config.password") +"\";"
                );
                return new FlinkKafkaProducer<>(sinkTopic, new SimpleStringSchema(), properties);
            }

            if (kafkaCluster.equals("baidu_cloud_prod")) {
                properties.put("sasl.jaas.config",
                        "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + config.getString("sasl.jaas.config.username") + "\" password=\""+ config.getString("sasl.jaas.config.password") +"\";"
                );
                return new FlinkKafkaProducer<>(sinkTopic, new SimpleStringSchema(), properties);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(config.getString("kafka.bootstrap.source.servers"));
    }
}
