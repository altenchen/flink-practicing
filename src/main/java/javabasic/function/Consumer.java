package javabasic.function;

/**
 * @author altenchen
 * @time 2021/1/26
 * @description 功能
 */
public class Consumer {
    
    public static void main(String[] args) {
        //创建字符串对象
        StringBuilder builder = new StringBuilder("sb字符串后面将会跟随####");
        //声明函数对象 consumer
        java.util.function.Consumer<StringBuilder> consumer = (str)-> str.append("TestSb");
        //调用Consumer.accept()方法接收参数
        consumer.accept(builder);
        System.out.println(builder.toString());
    }
    
}
