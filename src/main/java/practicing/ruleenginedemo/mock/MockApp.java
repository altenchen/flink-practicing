package practicing.ruleenginedemo.mock;

import practicing.ruleenginedemo.model.AtomicRule;
import practicing.ruleenginedemo.model.EventRecord;
import practicing.ruleenginedemo.model.RuleParam;

import java.util.ArrayList;
import java.util.List;

public class MockApp {

    private static final String expression = "mileage > 10 && current > 20";

    public static void main(String[] args) {
        AtomicRule atomicRule = mockAtomicRule();

        mockRuleParam(atomicRule);
    }

    /**
     * 原子规则
     *
     * @return
     */
    private static AtomicRule mockAtomicRule() {
        return AtomicRule.builder()
                .expression(expression)
                .targetTriggerCount(3)
                .targetCancelCount(3)
                .build();
    }

    /**
     * 规则封装
     *
     * @param atomicRule
     * @return
     */
    private static RuleParam mockRuleParam(AtomicRule atomicRule) {
        List<AtomicRule> atomicRules = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            atomicRules.add(atomicRule);
        }
        return RuleParam.builder()
                .ruleId("test_rule_id_001")
                .ruleName("test_rule_name_001")
                .patternType(0)
                .applyVehicleModel("ALL")
                .applyVehicle("ALL")
                .isEffective(0)
                .isDelete(0)
                .expressionList(atomicRules)
                .level(3)
                .build();
    }

    /**
     * 模拟T-box事件
     *
     * @return
     */
    private static EventRecord buildRecord(String vin, double mileage, double current, String time) {
        return EventRecord.builder()
                .vin(vin)
                .mileage(mileage)
                .current(current)
                .time(time)
                .build();
    }

    private static List<EventRecord> buildBatchEventRecord(int recordNum, double mileage, double current, String time) {
        ArrayList<EventRecord> records = new ArrayList<>(recordNum);
        int mileageIndex = 10;
        int currentIndex = 1;
        int timeIndex = 10*1000; //秒
        for (int i = 0; i < recordNum; i++) {
            records.add(buildRecord(
                    "test_vin_001",
                    mileage + i*mileageIndex,
                    current + i*currentIndex,
                    time + timeIndex
            ));
        }
        return records;
    }
}
