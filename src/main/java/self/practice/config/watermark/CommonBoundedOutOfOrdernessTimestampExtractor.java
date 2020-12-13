package self.practice.config.watermark;

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author altenchen
 * @create 功能
 */
public class CommonBoundedOutOfOrdernessTimestampExtractor extends BoundedOutOfOrdernessTimestampExtractor<WaterMark> {

    public CommonBoundedOutOfOrdernessTimestampExtractor(Time maxOutOfOrderness) {
        super(maxOutOfOrderness);
    }

    /**
     * Extracts the timestamp from the given element.
     *
     * @param element The element that the timestamp is extracted from.
     * @return The new timestamp.
     */
    @Override
    public long extractTimestamp(WaterMark element) {
        return element.extractTimestamp();
    }
}
