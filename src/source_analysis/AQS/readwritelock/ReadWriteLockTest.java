package source_analysis.AQS.readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-08-09 1:20
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        /**
         * 读写锁可以从写锁降级成读写，
         * 但如果从读锁升级成写锁有可能会造成死锁
         *
         * 如果有两个读锁同时升级为写锁。那么只有一个能升级成功。但是这两个线程同时拥有读锁。其中一个线程还一直在申请写锁。这就会造成死锁。
         * 但是锁降级是可以的。因为写锁只有一个线程占有。
         *
         * http://www.zzvips.com/article/193204.html
         * 假如只有一个线程t1，当t1已经获取读锁之后，再次获取写锁，因为写锁在加锁时判断到当前锁已经被加过读锁，
         * 读写互斥，所以写锁会等待读锁释放之后再加锁。
         * 但是因为读锁是被当前线程持有的，所以这个等待会无限的等待下去，最后就成了死锁。
         *
         * a读，b读，a写，发现有读锁未释放，排队去。。。
         * b写，也发现读锁未释放，排队去。。。
         * 此时a和b都被park了，也没人来解锁，也没人来唤醒他们
         *
         *
         * https://blog.csdn.net/weixin_42103620/article/details/117353744 源码
         */
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.readLock().lock();
        readWriteLock.readLock().lock();

        readWriteLock.readLock().unlock();

        readWriteLock.writeLock().lock();
        readWriteLock.writeLock().unlock();
    }

}
