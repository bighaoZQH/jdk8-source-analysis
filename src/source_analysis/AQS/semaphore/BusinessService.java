package source_analysis.AQS.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-30 19:02
 */
public class BusinessService {

    private Semaphore semaphore = new Semaphore(10, true);

    // 服务并发控制
    private void service() {
        try {
            semaphore.acquire();
            // 执行业务...
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

}
