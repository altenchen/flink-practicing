package practicing.asyncio;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

/**
 * @author: altenchen
 * @Date: 2022/7/14
 * @description: 获取hbase维表数据
 */
public class DimUtil {
    public static JSONObject getDimInfo(Connection conn, String tableName, String vin) throws Exception {

        Table table = conn.getTable(TableName.valueOf(tableName));
        Result result = table.get(new Get(vin.getBytes()));

        //result.advance()是否有下一个cell
        //result.current()获取当前cell
        JSONObject obj = JSONUtil.createObj();
        while (result.advance()) {
            Cell currentCell = result.current();
            byte[] qualifier = CellUtil.cloneQualifier(currentCell);
            byte[] value = CellUtil.cloneValue(currentCell);
            obj.put(new String(qualifier), new String(value));
        }

        return obj;
    }
}

