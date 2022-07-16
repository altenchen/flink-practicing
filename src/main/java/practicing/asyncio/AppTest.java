package practicing.asyncio;

import org.apache.flink.api.common.functions.util.ListCollector;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import practicing.asyncio.model.JoinDim32960Model;
import practicing.util.PropertiesUtil;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author: altenchen
 * @Date: 2022/7/16
 * @description:
 */
public class AppTest {
    public static void main(String[] args) {
        DataStream<JoinDim32960Model> collector = null;
        SingleOutputStreamOperator<JoinDim32960Model> joinDimStream = AsyncDataStream.unorderedWait(
                collector,
                new DimAsyncFunction(PropertiesUtil.getStringValue("hbase.dim.table.name")),
                PropertiesUtil.getLongValue("hbase.async.wait.time.mil.sec"),
                TimeUnit.MILLISECONDS
        );
    }
}
