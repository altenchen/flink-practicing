package func.rpc;

/**
 * @author altenchen
 * @time 2020/12/21
 * @description 功能
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello " + name;
    }

    @Override
    public String hi(String msg) {
        return "Hi, " + msg;
    }
}
