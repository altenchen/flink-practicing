package self.practice.kafkaSource.config.watermark;

/**
 * @author altenchen
 * @create 功能
 */
public interface WaterMark {
    /**
     *
     * @return
     */
    Long extractTimestamp();
}
