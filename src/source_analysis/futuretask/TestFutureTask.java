package source_analysis.futuretask;

import java.util.concurrent.*;

/**
 * @version 1.0
 * @author: bighao周启豪
 * @date 2020/10/24 19:02
 */
public class TestFutureTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future f1 = executorService.submit(new CallTest());
        f1.get();
        executorService.submit(new RunTest());
    }

    static class CallTest implements Callable {
        @Override
        public Object call() throws Exception {
            return null;
        }
    }

    static class RunTest implements Runnable {
        @Override
        public void run() {

        }
    }

}
