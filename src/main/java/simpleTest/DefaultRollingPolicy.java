//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package simpleTest;

import java.io.IOException;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
import org.apache.flink.streaming.api.functions.sink.filesystem.RollingPolicy;
import org.apache.flink.util.Preconditions;

@PublicEvolving
public final class DefaultRollingPolicy<IN, BucketID> implements RollingPolicy<IN, BucketID> {
    private static final long serialVersionUID = 1L;
    private static final long DEFAULT_INACTIVITY_INTERVAL = 60000L;
    private static final long DEFAULT_ROLLOVER_INTERVAL = 60000L;
    private static final long DEFAULT_MAX_PART_SIZE = 134217728L;
    private final long partSize;
    private final long rolloverInterval;
    private final long inactivityInterval;

    private DefaultRollingPolicy(long partSize, long rolloverInterval, long inactivityInterval) {
        Preconditions.checkArgument(partSize > 0L);
        Preconditions.checkArgument(rolloverInterval > 0L);
        Preconditions.checkArgument(inactivityInterval > 0L);
        this.partSize = partSize;
        this.rolloverInterval = rolloverInterval;
        this.inactivityInterval = inactivityInterval;
    }

    public boolean shouldRollOnCheckpoint(PartFileInfo<BucketID> partFileState) throws IOException {
        return partFileState.getSize() > this.partSize;
    }

    public boolean shouldRollOnEvent(PartFileInfo<BucketID> partFileState, IN element) throws IOException {
        return partFileState.getSize() > this.partSize;
    }

    public boolean shouldRollOnProcessingTime(PartFileInfo<BucketID> partFileState, long currentTime) {
        return currentTime - partFileState.getCreationTime() >= this.rolloverInterval || currentTime - partFileState.getLastUpdateTime() >= this.inactivityInterval;
    }

    public long getMaxPartSize() {
        return this.partSize;
    }

    public long getRolloverInterval() {
        return this.rolloverInterval;
    }

    public long getInactivityInterval() {
        return this.inactivityInterval;
    }

    public static PolicyBuilder builder() {
        return new PolicyBuilder(134217728L, 60000L, 60000L);
    }

    /** @deprecated */
    @Deprecated
    public static PolicyBuilder create() {
        return builder();
    }

    @PublicEvolving
    public static final class PolicyBuilder {
        private final long partSize;
        private final long rolloverInterval;
        private final long inactivityInterval;

        private PolicyBuilder(long partSize, long rolloverInterval, long inactivityInterval) {
            this.partSize = partSize;
            this.rolloverInterval = rolloverInterval;
            this.inactivityInterval = inactivityInterval;
        }

        public PolicyBuilder withMaxPartSize(long size) {
            Preconditions.checkState(size > 0L);
            return new PolicyBuilder(size, this.rolloverInterval, this.inactivityInterval);
        }

        public PolicyBuilder withInactivityInterval(long interval) {
            Preconditions.checkState(interval > 0L);
            return new PolicyBuilder(this.partSize, this.rolloverInterval, interval);
        }

        public PolicyBuilder withRolloverInterval(long interval) {
            Preconditions.checkState(interval > 0L);
            return new PolicyBuilder(this.partSize, interval, this.inactivityInterval);
        }

        public <IN, BucketID> DefaultRollingPolicy<IN, BucketID> build() {
            return new DefaultRollingPolicy(this.partSize, this.rolloverInterval, this.inactivityInterval);
        }
    }
}
