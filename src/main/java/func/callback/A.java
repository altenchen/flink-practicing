package func.callback;

/**
 * @author altenchen
 * @time 2021/1/26
 * @description 功能
 */
public class A {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new B().handle(
                        "handle something",
                        data-> {
                            System.out.println(getClass().getName() + "系统的回调方法，收到数据：" + data);
                        });
            }
        }).start();
        System.out.println("A系统异步回调，先做其他事情");
    }


}
