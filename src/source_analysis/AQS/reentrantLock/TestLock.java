package source_analysis.AQS.reentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-12 22:15
 */
public class TestLock {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        // 获取锁，成功往下执行，没有获取到会阻塞一直到成功为止
        lock.lock();

        try {
            // 业务代码..

        } finally {
            lock.unlock();
        }

    }

}
