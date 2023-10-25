package func.rpc.simpledemo;

/**
 * @author altenchen
 * @time 2020/12/21
 * @description 功能
 */
public class ConsumerApp {

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();
        int result = calculator.add(1, 2);
    }

}
