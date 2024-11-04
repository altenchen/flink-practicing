//package simpleTest;
//
//import org.apache.flink.annotation.PublicEvolving;
//import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
//import org.apache.flink.streaming.api.functions.sink.filesystem.RollingPolicy;
//import org.apache.flink.util.Preconditions;
//
//import java.io.IOException;
//
///**
// * @author: altenchen
// * @Date: 2024/11/1
// * @description:
// */
//public class CustomRollingPolicy implements RollingPolicy<VehdataHiveModel, String> {
//
//    private static final long serialVersionUID = 1L;
//    private static final long DEFAULT_INACTIVITY_INTERVAL = 60000L;
//    private static final long DEFAULT_ROLLOVER_INTERVAL = 60000L;
//    private static final long DEFAULT_MAX_PART_SIZE = 134217728L;
//    private final long partSize;
//    private final long rolloverInterval;
//    private final long inactivityInterval;
//
//    private CustomRollingPolicy(long partSize, long rolloverInterval, long inactivityInterval) {
//        Preconditions.checkArgument(partSize > 0L);
//        Preconditions.checkArgument(rolloverInterval > 0L);
//        Preconditions.checkArgument(inactivityInterval > 0L);
//        this.partSize = partSize;
//        this.rolloverInterval = rolloverInterval;
//        this.inactivityInterval = inactivityInterval;
//    }
//
//    public boolean shouldRollOnCheckpoint(PartFileInfo<String> partFileState) throws IOException {
//        return partFileState.getSize() > this.partSize;
//    }
//
//    public boolean shouldRollOnEvent(PartFileInfo<String> partFileState, VehdataHiveModel element) throws IOException {
//        return partFileState.getSize() > this.partSize;
//    }
//
//    public boolean shouldRollOnProcessingTime(PartFileInfo<String> partFileState, long currentTime) {
//        return currentTime - partFileState.getCreationTime() >= this.rolloverInterval || currentTime - partFileState.getLastUpdateTime() >= this.inactivityInterval;
//    }
//
//    public long getMaxPartSize() {
//        return this.partSize;
//    }
//
//    public long getRolloverInterval() {
//        return this.rolloverInterval;
//    }
//
//    public long getInactivityInterval() {
//        return this.inactivityInterval;
//    }
//
//    public static CustomRollingPolicy.PolicyBuilder builder() {
//        return new CustomRollingPolicy.PolicyBuilder(134217728L, 60000L, 60000L);
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public static CustomRollingPolicy.PolicyBuilder create() {
//        return builder();
//    }
//
//    @PublicEvolving
//    public static final class PolicyBuilder {
//        private final long partSize;
//        private final long rolloverInterval;
//        private final long inactivityInterval;
//
//        private PolicyBuilder(long partSize, long rolloverInterval, long inactivityInterval) {
//            this.partSize = partSize;
//            this.rolloverInterval = rolloverInterval;
//            this.inactivityInterval = inactivityInterval;
//        }
//
//        public CustomRollingPolicy.PolicyBuilder withMaxPartSize(long size) {
//            Preconditions.checkState(size > 0L);
//            return new CustomRollingPolicy.PolicyBuilder(size, this.rolloverInterval, this.inactivityInterval);
//        }
//
//        public CustomRollingPolicy.PolicyBuilder withInactivityInterval(long interval) {
//            Preconditions.checkState(interval > 0L);
//            return new CustomRollingPolicy.PolicyBuilder(this.partSize, this.rolloverInterval, interval);
//        }
//
//        public CustomRollingPolicy.PolicyBuilder withRolloverInterval(long interval) {
//            Preconditions.checkState(interval > 0L);
//            return new CustomRollingPolicy.PolicyBuilder(this.partSize, interval, this.inactivityInterval);
//        }
//
//        public <VehdataHiveModel, String> CustomRollingPolicy<simpleTest.VehdataHiveModel, java.lang.String> build() {
//            return new CustomRollingPolicy(this.partSize, this.rolloverInterval, this.inactivityInterval);
//        }
//
//    }
////
////    @Override
////    public boolean shouldRollOnCheckpoint(PartFileInfo<String> partFileInfo) throws IOException {
////        return false;
////    }
////
////    @Override
////    public boolean shouldRollOnEvent(PartFileInfo<String> partFileInfo, VehdataHiveModel vehdataHiveModel) throws IOException {
////        return false;
////    }
////
////    @Override
////    public boolean shouldRollOnProcessingTime(PartFileInfo<String> partFileInfo, long l) throws IOException {
////        return false;
////    }
//}
