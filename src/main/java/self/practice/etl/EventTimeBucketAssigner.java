package self.practice.etl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.io.SimpleVersionedSerialization;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author altenchen
 * @time 2020/12/17
 * @description 功能
 */
public class EventTimeBucketAssigner implements BucketAssigner<String, String> {
    
    @Override
    public String getBucketId(String element, Context context) {
        String time = String.valueOf(JSONUtil.parseObj(element).get("TIME"));
        String partitionValue = DateUtil.format(DateUtil.parse(time, "yyyyMMddHHmmss"), "yyyyMMdd");
        return "dt=" + partitionValue;
    }
    
    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return new CustomStringSerializer();
    }
    
    private class CustomStringSerializer implements SimpleVersionedSerializer<String> {
    
        final int version = 77;
        
        @Override
        public int getVersion() {
            return version;
        }
    
        @Override
        public byte[] serialize(String obj) {
            return obj.getBytes(StandardCharsets.UTF_8);
        }
    
        @Override
        public String deserialize(int version, byte[] serialized) throws IOException {
            if (version != 77) {
                throw new IOException("version mismatch");
            }
            return new String(serialized, StandardCharsets.UTF_8);
        }
    }
}
