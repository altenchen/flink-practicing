package practicing.ruleenginedemo.model;

import lombok.Data;

@Data
public class AlarmNotice extends AlarmState {

    private String vin;

    private String vid;

    /**
     * 全局唯一
     */
    private String msgId;




}
