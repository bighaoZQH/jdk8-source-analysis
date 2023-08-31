package source_analysis.AQS.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-08-08 14:31
 */
public class BlockingQueueTest {

    public static void main(String[] args) throws Exception {
        BlockingQueue<String> queue = new ArrayBlockingQueue(16);
        // 满了会抛异常，offer只会返回false
        queue.add("a");
        queue.offer("a");
        // 满了会阻塞住，直到put成功
        queue.put("a");

        // 没有抛异常，poll会返回null
        queue.remove();
        queue.poll();
        // 没有会阻塞住，直到获取成功
        queue.take();
    }
}
