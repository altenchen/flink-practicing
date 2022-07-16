package practicing.asyncio;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import practicing.util.PropertiesUtil;

/**
 * @author: altenchen
 * @Date: 2022/7/14
 * @description: hbase工具类
 */
public class HbaseUtil {

    private static Configuration conf = null;
    private static Connection conn = null;

    private static class HbaseClientInstance{
        private static final HbaseUtil INSTANCE = new HbaseUtil();
    }

    public static HbaseUtil getInstance() {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", PropertiesUtil.getStringValue("hbase.zookeeper.quorum"));
        conf.set("hbase.zookeeper.property.client", PropertiesUtil.getStringValue("hbase.zookeeper.property.client"));
        try{
            conn = ConnectionFactory.createConnection(conf);
        }catch (Exception e){
            e.printStackTrace();
        }
        return HbaseClientInstance.INSTANCE;
    }

    public Connection getConn() {
        return conn;
    }

}
