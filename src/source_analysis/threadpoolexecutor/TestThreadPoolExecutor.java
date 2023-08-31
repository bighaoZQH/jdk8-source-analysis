package source_analysis.threadpoolexecutor;

import java.util.concurrent.*;

/**
 * @version 1.0
 * @author: bighao周启豪
 * @date 2020/10/25 19:59
 */
public class TestThreadPoolExecutor {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(0,
                        10,
                        10,
                        TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(100),
                        new ThreadPoolExecutor.AbortPolicy()
                        );

        threadPoolExecutor.execute(() -> {
            System.out.println("a");
        });

        Future<?> submit0 = threadPoolExecutor.submit(() -> {
            System.out.println("a");
        });

        Future<String> submit1 = threadPoolExecutor.submit(() -> {
            System.out.println("a");
        }, "runnable default result");

        Future<String> submit = threadPoolExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("callable..");
                return "callable";
            }
        });

        threadPoolExecutor.shutdown();
        threadPoolExecutor.shutdownNow();
    }

}
