package source_analysis.AQS.countDownLacth;

import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-25 18:51
 */
public class TestCountDownLatch {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.countDown();
        countDownLatch.await();
    }

}
