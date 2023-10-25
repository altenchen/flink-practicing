package practicing.ruleenginedemo;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import practicing.ruleenginedemo.model.AlarmState;
import practicing.util.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;

public class RuleEngineApp {

    private static Map<String, AlarmState> alarmState = new HashMap<>();

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(PropertiesUtil.getStringValue("kafka.bootstrap.servers"))
                .setTopics(PropertiesUtil.getStringValue("kafka.source.topic"))
                .setGroupId(PropertiesUtil.getStringValue("kafka.group.id"))
                // Start from committed offset, also use EARLIEST as reset strategy if committed offset doesn't exist
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setProperty("partition.discovery.interval.ms", "5000") // discover new partitions per 10 seconds
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStreamSource<String> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");



        env.execute("Window WordCount");
    }

}
