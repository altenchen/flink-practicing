package practicing.ruleenginedemo.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 原子规则模型封装
 */
@Data
@Builder
public class RuleParam {

    private String ruleId;

    private String ruleName;

    /**
     * 0-连续次数；1-固定次序；
     */
    private int patternType;

    private String applyVehicleModel;

    private String applyVehicle;

    /**
     * 0-生效，1-不生效
     */
    private int isEffective;

    /**
     * 0-删除，1-不删除
     */
    private int isDelete;

    /**
     * 1，当pattern_type = 0，即为次数模式，只有一个原子规则；
     * 2，当pattern_type = 1，即为固定次序模式，则值为按次序排列的事件集合；
     */
    private List<AtomicRule> expressionList;



    private int level;
}
