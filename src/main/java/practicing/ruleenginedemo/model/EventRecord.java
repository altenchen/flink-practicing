package practicing.ruleenginedemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRecord {

    private String vin;

    private String time;

    private double mileage;

    private double current;
}
