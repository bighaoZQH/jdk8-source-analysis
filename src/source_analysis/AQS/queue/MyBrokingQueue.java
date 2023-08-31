package source_analysis.AQS.queue;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-20 23:12
 */
public interface MyBrokingQueue<T> {

    void put(T element) throws InterruptedException;

    T take() throws InterruptedException;

}
