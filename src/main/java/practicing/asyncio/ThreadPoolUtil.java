package practicing.asyncio;

import practicing.util.PropertiesUtil;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {
 
    private static ThreadPoolExecutor threadPoolExecutor = null;
 
    private ThreadPoolUtil(){}
 
    public static ThreadPoolExecutor getThreadPool(){
        if(threadPoolExecutor == null){
            synchronized(ThreadPoolUtil.class){
                if(threadPoolExecutor == null){
                    threadPoolExecutor = new ThreadPoolExecutor(
                            PropertiesUtil.getIntValue("hbase.dim.join.core.pool.size"),
                            PropertiesUtil.getIntValue("hbase.dim.join.max.pool.size"),
                            PropertiesUtil.getLongValue("hbase.dim.join.keep.alive.time.sec"),
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<>());
                }
            }
        }
        return threadPoolExecutor;
    }
 
}