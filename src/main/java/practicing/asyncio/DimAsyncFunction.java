package practicing.asyncio;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import practicing.asyncio.model.JoinDim32960Model;
import practicing.asyncio.model.VehicleDimModel;

/**
 * @author: altenchen
 * @Date: 2022/7/14
 * @description: hbase维表关联实现
 */
public class DimAsyncFunction extends AbstractDimAsyncFunction<JoinDim32960Model>{

    public DimAsyncFunction(String tableName) {
        super(tableName);
    }

    @Override
    public String getKey(JoinDim32960Model joinDim32960Model) {
        return joinDim32960Model.getVin();
    }

    @Override
    public void joinInfo(JoinDim32960Model joinDim32960Model, JSONObject dimInfo) {
        joinDim32960Model.setVehicleDimModel(JSONUtil.toBean(dimInfo, VehicleDimModel.class));
    }

}
