package practicing.asyncio.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author: altenchen
 * @Date: 2022/7/14
 * @description:
 */
@Data
@Builder
public class VehicleDimModel {

     private String vin;

     private String brand;

     private String model_sn;

     private String plate;

     private String tbox_sn;

     private String iccid;

     private String imsi;

     private String mdn;

     private String owner_name;

     private String owner_phone;

     private String owner_cert_type;

     private String owner_cert_no;

     private String owner_address;

     private String owner_urgent_contact_name;


}
