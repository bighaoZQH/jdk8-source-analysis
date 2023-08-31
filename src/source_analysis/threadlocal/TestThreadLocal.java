package source_analysis.threadlocal;

/**
 * @version 1.0
 * @author: bighao周启豪
 * @date 2020/10/23 17:56
 */
public class TestThreadLocal {

    public static void main(String[] args) {
        ThreadLocal<Object> objectThreadLocal = new ThreadLocal<Object>() {
            @Override
            protected Object initialValue() {
                return new Object();
            }
        };
        objectThreadLocal.set(new Object());
        objectThreadLocal.get();
    }

}
