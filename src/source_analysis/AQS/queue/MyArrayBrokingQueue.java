package source_analysis.AQS.queue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-20 23:13
 */
public class MyArrayBrokingQueue implements MyBrokingQueue {
    // 线程并发控制
    private final Lock lock = new ReentrantLock();
    /**
     * 当生存者线程生存数据时，它会检查当前queues是否已经满了，如果已经满了，需要将当前生产者线程调用notFull.await()，
     * 进入到notFull条件队列挂起，等待消费者线程消费数据时唤醒
     */
    private final Condition notFull = lock.newCondition();
    /**
     * 当前消费者线程消费数据时，发现队列中没有数据，那么当前消费者线程会调用notEmpty.await()，进入到notEmpty条件队列中挂起，
     * 等待生产者生产数据时唤醒
     */
    private final Condition notEmpty = lock.newCondition();
    // 存任务的队列
    private final Object[] items;
    // 队列长度
    private final int size;

    // count-当前队列中可以被消费的数据量
    private int count;
    // putptr-记录生产者存放数据的下一次位置，每个生产者生产完一个数据后，会将putptr++
    private int putIndex;
    // takeptr-记录消费者消费数据的下一次位置，每个消费者消费完数据后，会将takeptr++
    private int takeIndex;

    public MyArrayBrokingQueue(int capacity) {
        this.size = capacity;
        this.items = new Object[capacity];
    }

    @Override
    public void put(Object element) throws InterruptedException {
        lock.lock();
        try {
            // 第一件事，判断下当前queue是否已经满了
            if (count == size) {
                notFull.await();
            }
            // 队列未满
            this.items[putIndex] = element;
            // 如果putIndex超过了最大下标，则继续从0开始,循环利用..
            if (++putIndex == items.length) {
                putIndex = 0;
            }
            // 更新任务数量
            count++;
            // 任务入队后，要唤醒一个消费者线程，因此给notEmpty一个唤醒信号
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            // 第一件事，判断下当前队列是否有数据可以被消费
            if (count == 0) {
                notEmpty.await();
            }
            // 执行到这里，说明队列有数据可以被消费
            Object element = this.items[takeIndex];
            // help gc
            items[takeIndex] = null;
            // 到尾了 再从头开始拿
            if (++takeIndex == size) {
                takeIndex = 0;
            }
            // 更新任务数量
            count--;
            // 消费了一个任务后，需要给notFull一个唤醒信号
            notFull.signal();
            return element;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        MyBrokingQueue<Integer> queue = new MyArrayBrokingQueue(10);
        Thread producer = new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                if (i == 10) i = 0;
                try {
                    System.out.println("生产数据: " + i);
                    queue.put(Integer.valueOf(i));
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {}
            }
        });
        producer.start();

        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    Integer take = queue.take();
                    System.out.println("消费者消费数据: " + take);
                } catch (InterruptedException e) {}
            }
        });
        consumer.start();
    }
}
