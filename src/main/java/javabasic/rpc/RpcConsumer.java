package javabasic.rpc;

/**
 * @author altenchen
 * @time 2020/12/21
 * @description 功能
 */
public class RpcConsumer {
    public static void main(String[] args) {
        //用RpcFramework生成的HelloService的代理
        HelloService service = RpcFramework.refer(HelloService.class, "127.0.0.1", 1234);
        String hello = service.hello("World");
        System.out.println("客户端收到远程调用的结果：" + hello);
    }
}
