package javabasic.callback;

import java.util.concurrent.TimeUnit;

/**
 * @author altenchen
 * @time 2021/1/26
 * @description 功能
 */
public class B {
    
    int count = 20;
    
    public void handle(String data, Callback callback) {
        System.out.println("B系统接收到来自A系统的请求消息：" + data + ", 开始处理data...");
        int remaining;
        int initialTime = count;
        while (true) {
            remaining = count;
            if (remaining == 0) {
                break;
            }
    
            System.out.println("Remaining " + count + " second");
            
            countDown();
            try {
                //sleep 10 秒，模拟满足某些条件
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        callback.onResponse("B系统已将A系统发来的请求处理完成，耗时【" + initialTime + "】秒，响应Success.");
    }
    
    private void countDown() {
        count--;
    }
    
}
