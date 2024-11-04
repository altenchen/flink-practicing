package simpleTest;

import lombok.Builder;
import lombok.Data;

/**
 * @author: altenchen
 * @Date: 2024/10/31
 * @description:
 */
@Data
@Builder
public class VehdataHiveModel {

    public String vin;
    public String gather_date_time;
    public String msg_id;
    public String reported_time;
    public String receive_time;
    public String receive_broker_time;
    public String trace_id;
    public String event_id;
    public String hour_date;
    public String equipment_id;
    public String service_id;
    public String sub_function;
    public String vehicle_model;
    public Integer equipment_id_type;
    public Integer body_data_type;
    public String protocol_version;
    public String dids;
    public String msg_data;
    public String dt;
    public String msg_name;

}
