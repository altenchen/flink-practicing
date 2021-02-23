package javabasic.function;

/**
 * @author altenchen
 * @time 2021/1/26
 * @description 功能
 */
public class BiConsumer {
    
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        //声明函数对象 consumer
        java.util.function.BiConsumer<String, String> consumer = (str1, str2)-> {
            //拼接字符串
            System.out.println("回调触发");
            builder.append(str1);
            builder.append(str2);
        };
        //调用consumer.accept()方法接收参数
        System.out.println("回调以前");
        consumer.accept("我是参数01","，我是参数02。我们被BiConsumer.accept(T,V)接收并处理了");
        System.out.println(builder.toString());
    
        consumer.andThen((str3, str4)-> {
            builder.append("\\/n");
            builder.append("andThen1");
            builder.append("andThen2");
        });
    
        System.out.println("andThen调用前");
        System.out.println("andThen调用后");
        builder.append("\\/n");
        System.out.println(builder.toString());
    }
    
    
}
