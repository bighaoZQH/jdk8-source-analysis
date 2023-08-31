package source_analysis;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: bighao周启豪
 * @Date: 2020/3/3 21:40
 * @Version 1.0
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello world-");
        ReentrantLock lock = new ReentrantLock(true);
        lock.lock();
        lock.unlock();
        lock.lockInterruptibly();



    }
}
