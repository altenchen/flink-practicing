package practicing.ruleenginedemo.model;


import lombok.Data;

import java.util.List;

@Data
public class AlarmState{

    private String ruleId;

    private String ruleName;

    private String level;

    /**
     * 0-正常；1-触发；2-持续；3-结束；
     */
    private String status;

    /**
     * 0-连续次数；1-固定次序；
     */
    private int patternType;

    /**
     * 1，当pattern_type = 0，即为次数模式，只有一个原子规则；
     * 2，当pattern_type = 1，即为固定次序模式，则值为按次序排列的事件集合；
     */
    private List<AtomicRule> expressionList;

    private String startTime;

    private String startLocation;

    private String endTime;

    private String endLocation;

}
