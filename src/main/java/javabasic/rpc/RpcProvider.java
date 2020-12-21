package javabasic.rpc;

/**
 * @author altenchen
 * @time 2020/12/21
 * @description 功能
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        //RPC框架将服务暴露出来，供客户端消费
        RpcFramework.export(service, 1234);
    }
}
