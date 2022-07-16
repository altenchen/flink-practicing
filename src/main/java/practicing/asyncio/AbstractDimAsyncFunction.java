package practicing.asyncio;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.hadoop.hbase.client.Connection;

import java.text.ParseException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: altenchen
 * @Date: 2022/7/14
 * @description: hbase维表关联抽象类
 */
@Slf4j
public abstract class AbstractDimAsyncFunction<T> extends RichAsyncFunction<T, T> {
 
    private Connection connection;
    private ThreadPoolExecutor threadPoolExecutor;
    private String tableName;
 
    public AbstractDimAsyncFunction(String tableName) {
        this.tableName = tableName;
    }
 
    @Override
    public void open(Configuration parameters) {
        connection = HbaseUtil.getInstance().getConn();
        //线程池
        threadPoolExecutor = ThreadPoolUtil.getThreadPool();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null) {
            connection.close();
        }
    }

    public abstract String getKey(T t);
 
    public abstract void joinInfo(T t, JSONObject dimInfo) throws ParseException;

    @Override
    public void asyncInvoke(T t, final ResultFuture<T> resultFuture){
        CompletableFuture.supplyAsync(
                () -> {
                    String rowkey = getKey(t);
                    JSONObject dimInfo = null;
                    try {
                        dimInfo = DimUtil.getDimInfo(connection, tableName, rowkey);
                    } catch (Exception e) {
                        log.error("查询hbase异常，rowkey=[{}]", rowkey);
                        throw new RuntimeException(e);
                    }
                    if(dimInfo != null){
                        try {
                            joinInfo(t, dimInfo);
                        } catch (ParseException e) {
                            log.error("补充纬度信息异常, dimInfo=[${}]", dimInfo);
                            throw new RuntimeException(e);
                        }
                    } else {
                        log.info("未关联到维度信息，rowkey=[{}]", rowkey);
                    }
                    return t;
                }
        , threadPoolExecutor).whenComplete(
                (response, exception) -> {
                    if (response != null) {
                        resultFuture.complete(Collections.singletonList(response));
                    } else {
                        resultFuture.completeExceptionally(exception);
                    }
                }
        );
    }

    @Override
    public void timeout(T input, ResultFuture<T> resultFuture) {
        //超时处理，后期可发往死信队列
//        resultFuture.completeExceptionally(new TimeoutException("hbase维表join超时，key = " + getKey(input)));
        log.error("hbase维表join超时，key = " + getKey(input));
    }
}