package practicing.ruleenginedemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtomicRule {

    private String ruleId;

    private String ruleName;

    /**
     * 原子规则表达式
     */
    private String expression;

    /**
     * 目标连续触发阈值
     */
    private int targetTriggerCount;

    /**
     * 实际连续触发值
     */
    private int actualTriggerCount;

    /**
     * 目标连续结束阈值
     */
    private int targetCancelCount;

    /**
     * 实际连续结束值
     */
    private int actualCancelCount;

    /**
     * 0-正常；1-触发；2-持续；3-结束；
     */
    private int atomicStatus;
}
