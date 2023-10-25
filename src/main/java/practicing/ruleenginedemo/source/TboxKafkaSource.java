package practicing.ruleenginedemo.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import practicing.util.PropertiesUtil;

public class TboxKafkaSource {

    public static void main(String[] args) {

    }

    public static KafkaSource<String> getTboxKafkaSource() {
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(PropertiesUtil.getStringValue("kafka.bootstrap.servers"))
                .setTopics(PropertiesUtil.getStringValue("kafka.source.topic"))
                .setGroupId(PropertiesUtil.getStringValue("kafka.group.id"))
                // Start from committed offset, also use EARLIEST as reset strategy if committed offset doesn't exist
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setProperty("partition.discovery.interval.ms", "5000") // discover new partitions per 10 seconds
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        return source;
    }






}
