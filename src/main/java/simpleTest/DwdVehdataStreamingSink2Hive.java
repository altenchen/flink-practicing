package simpleTest;

import com.alibaba.fastjson2.JSONObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.util.Collector;
import org.apache.flink.util.TimeUtils;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import practicing.util.CustomParquetAvroWriters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author: altenchen
 * @Date: 2024/10/30
 * @description:
 */
public class DwdVehdataStreamingSink2Hive {

    private static Config config = ConfigFactory.load();

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = FlinkEnvBuilder.initEnv();

        SingleOutputStreamOperator<String> vehDataSource = env.addSource(KafkaUtil.getKafkaConsumer(config.getString("kafka.dwd.vehdata.source.topic")))
                .setParallelism(config.getInt("filesink.source.parallelism"))
                .uid("vehdataSourceOperator")
                .name("vehdataSourceOperator");

        DataStream<VehdataHiveModel> sinkModelStream = vehDataSource.flatMap(new FlatMapFunction<String, VehdataHiveModel>() {
                    @Override
                    public void flatMap(String value, Collector<VehdataHiveModel> out) throws Exception {
                        try {
                            JSONObject inputObj = JSONObject.parseObject(value);
                            VehdataHiveModel resModel = VehdataHiveModel.builder()
                                    .vin(inputObj.getOrDefault("vin", null) + "")
                                    .gather_date_time(inputObj.getOrDefault("gatherDatetime", null) + "")
                                    .msg_id(inputObj.getOrDefault("msgId", null) + "")
                                    .reported_time(inputObj.getOrDefault("reportedTime", null) + "")
                                    .receive_time(inputObj.getOrDefault("receiveTime", null) + "")
                                    .receive_broker_time(inputObj.getOrDefault("receiveBrokerTime", null) + "")
                                    .trace_id(inputObj.getOrDefault("traceId", null) + "")
                                    .event_id(inputObj.getOrDefault("eventId", null) + "")
                                    .hour_date(inputObj.getOrDefault("hourDate", null) + "")
                                    .equipment_id(inputObj.getOrDefault("equipmentID", null) + "")
                                    .service_id(inputObj.getOrDefault("serviceID", null) + "")
                                    .sub_function(inputObj.getOrDefault("subFunction", null) + "")
                                    .vehicle_model(inputObj.getOrDefault("vehicleModel", null) + "")
                                    .equipment_id_type(inputObj.getInteger("equipmentIDType"))
                                    .body_data_type(inputObj.getInteger("bodyDataType"))
                                    .protocol_version(inputObj.getOrDefault("protocolVersion", null) + "")
                                    .dids(inputObj.getOrDefault("dids", null) + "")
                                    .msg_data(inputObj.getOrDefault("msgData", null) + "")
                                    .dt(processDtColumn(inputObj.getOrDefault("gatherDatetime", null) + ""))
                                    .msg_name(inputObj.getOrDefault("msgName", null) + "")
                                    .build();
                            out.collect(resModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setParallelism(config.getInt("filesink.transfer.parallelism"))
                .uid("transferOperator")
                .name("transferOperator");


        StreamingFileSink<VehdataHiveModel> sink = StreamingFileSink
                .forBulkFormat(
                        new Path(config.getString("vehdata.hive.sink.path")),
                        CustomParquetAvroWriters.forReflectRecord(VehdataHiveModel.class,
                                config.getBoolean("filesink.compress.enable"),
                                CompressionCodecName.fromConf(config.getString("filesink.compress.codec"))
                        )
                )
                .withRollingPolicy(
                        OnCheckpointRollingPolicy.build()
                )
                .withBucketAssigner(new BucketAssigner<VehdataHiveModel, String>() {
                    @Override
                    public String getBucketId(VehdataHiveModel element, Context context) {
                        return "dt=" + element.dt + "/msg_name=" + element.msg_name;
                    }

                    @Override
                    public SimpleVersionedSerializer<String> getSerializer() {
                        return SimpleVersionedStringSerializer.INSTANCE;
                    }
                })
                .build();

        sinkModelStream.addSink(sink)
                .setParallelism(config.getInt("filesink.sink.parallelism"))
                .uid("fileSinkOperator")
                .name("fileSinkOperator")
        ;

        env.execute("VehdataStreamingFileSink");

    }

    private static String processDtColumn(String gatherDatetime) {
        // 处理 dt 字段
        LocalDate gatherDate = LocalDate.parse(gatherDatetime.substring(0, 10), dateFormatter);
        LocalDate currentDate = LocalDate.now();

        if (!gatherDate.isBefore(currentDate.minus(2, ChronoUnit.DAYS)) &&
                !gatherDate.isAfter(currentDate.plus(1, ChronoUnit.DAYS))) {
            return gatherDate.format(dateFormatter);
        } else {
            return "2123-01-01";
        }
    }



}
