package func.function;


/**
 * @author altenchen
 * @time 2021/1/26
 * @description 功能
 */
public class BiConsumer {

    public static void main(String[] args) {
        // 创建一个BiConsumer实例
        java.util.function.BiConsumer<String, Integer> printLength = (str, length) -> System.out.println(str + " 的长度为 " + length);

        // 使用accept方法接受两个参数并执行操作
//        printLength.accept("Hello1", 5);

        // 使用andThen方法组合两个BiConsumer实例
        java.util.function.BiConsumer<String, Integer> printUpperCase = (str, length) -> {
            String upperCaseStr = str.toUpperCase();
            System.out.println(upperCaseStr + " 的长度为 " + length);
        };

        java.util.function.BiConsumer<String, Integer> printInfo = printLength.andThen(printUpperCase);


        java.util.function.BiConsumer<String, Integer> printUpperCase1 = (str, length) -> {
            String upperCaseStr = str.toUpperCase();
            System.out.println(upperCaseStr + " 的长度为 " + length + "X");
        };

        java.util.function.BiConsumer<String, Integer> stringIntegerBiConsumer = printInfo.andThen(printUpperCase1);

        // 使用组合后的BiConsumer实例执行操作
        stringIntegerBiConsumer.accept("Hello", 5);
    }


}
