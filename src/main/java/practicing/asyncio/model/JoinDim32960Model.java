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
public class JoinDim32960Model {

    private String vin;

    private Source32960Model source32960Model;

    private VehicleDimModel vehicleDimModel;

}
