package source_analysis.AQS.reentrantLock;

import sun.misc.Unsafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * @description:
 * @author: ZhouQiHao bighao1996@163.com
 * @date: 2022-04-12 22:19
 */
public class MyReentrantLock implements MyLock {

    /**
     * 锁的是什么？
     * 资源 -> state
     * 0 - 未加锁  >0 - 已加锁(可重入)
     */
    private volatile int state;

    /**
     * 独占模式 - 同一时刻只能有一个线程获取锁，其他线程会被阻塞
     * 当前独占锁的线程
     */
    private Thread exclusiveOwnerThread;

    /**
     * 需要两个引用来方便维护阻塞队列
     * head比较特殊 - 指向当前占用锁的线程
     */
    private Node head;
    private Node tail;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }


    /**
     * 阻塞的线程怎么办？
     * 封装成Node节点，然后放入FIFO队列(双向)
     */
    static final class Node {
        Thread thread;
        Node prev;
        Node next;

        public Node() {
        }

        public Node(Thread thread) {
            this.thread = thread;
        }
    }

    /**
     * 公平锁 - 先来后到
     * lock的过程是怎么样的？
     * 1.线程进来后发现 当前state == 0,这时候可以尝试抢锁（如果是公平锁要看等待队列是否为空）
     * 2...              state 》 0, 当前线程入队
     */
    @Override
    public void lock() {
        acquire(1);
    }

    /**
     * 竞争资源
     * 1.尝试获取锁，成功则占用锁然后返回
     * 2.抢占失败，阻塞当前线程
     * <p>
     * 抢占失败要干什么呢？
     * 1.将当前线程封装成Node，加入到阻塞队列
     * 2.park当前线程
     * <p>
     * 那么等待线程被唤醒后，要干什么呢？
     * 1.检查当前节点是否为head.next节点（head.next节点是拥有抢占锁权限的线程，其他node都没有这个权限）
     * 2.抢占
     * 2.1成功: 1.将当前ndoe设置为head, 老head出队
     * 2.2失败: 2.失败继续park, 等待唤醒
     * ================>
     * 1.添加到阻塞队列的逻辑  addWaiter()
     * 2.park和竞争资源的逻辑 acquireQueued()
     */
    private void acquire(int acquires) {
        // 获取锁成功了就直接返回了，没有获取成功就要去入队
        if (!tryAcquire(acquires)) {
            acquireQueued(addWaiter(), acquires);
        }
    }

    private void acquireQueued(Node node, int acquires) {
        // 只有当前node成功获取到锁后，才会跳出自旋
        for (;;) {
            Node prev = node.prev;
            // 只有head.next才可以去抢占锁
            if (prev == head && tryAcquire(acquires)) {
                // 更新head节点为当前node，再把老的head出队
                setHead(node);
                prev.next = null;
                return;
            }
            // 将当前线程挂起
            System.out.printf("线程: " + Thread.currentThread().getName() + "-挂起");
            LockSupport.park();
            System.out.printf("线程: " + Thread.currentThread().getName() + "-唤醒");
        }
    }

    /**
     * 当前线程入队
     * 如何入队呢？
     * 1.找到newNode的前置节点prev
     * 2.更新newNode.prev = 前置节点
     * 3.cas更新tail为newNode
     * 4.更新prev.next = newNode
     */
    private Node addWaiter() {
        Node newNode = new Node(Thread.currentThread());
        // 如果已经有等待者队列了，当前node不是第一个入队的node
        Node prev = tail;
        // 队列不为空cas尝试快速入队
        if (tail != null) {
            newNode.prev = prev;
            if (compareAndSetTail(prev, newNode)) {
                // 入队成功 aqs是一个双向链表
                prev.next = newNode;
                return newNode;
            }
        }
        // 如果队列为空，或者cas入队失败，则走enq自旋入队
        enq(newNode);
        return newNode;
    }


    /**
     * 自旋入队，只有成功后才返回  2.cas失败的时候
     */
    private void enq(Node node) {
        for (; ; ) {
            // 第一种情况，队列为空，那么当前线程是第一个抢占锁失败的线程
            // 这时当前线程需要帮抢占锁成功的线程 设置成head节点，然后自己再入队
            if (tail == null) {
                if (compareAndSetHead(new Node())) {
                    tail = head;
                    // 注意没有返回，会继续自旋
                } else {
                    // 第二种情况，当前队列有node了，当前线程是追加的过程
                    Node prev = tail;
                    if (compareAndSetTail(prev, node)) {
                        prev.next = node;
                        node.prev = prev;
                        return;
                    }
                }
            }
        }
    }

    /**
     * 尝试获取锁，不会阻塞线程
     *
     * @return true-加锁成功 fasle-加锁失败
     */
    private boolean tryAcquire(int acquires) {
        int c = getState();
        if (c == 0) {
            // 看到state是0可以去抢占锁吗？ 不行，因为是公平锁，要看下队列里是否有等待者，没有等待者才可以通过cas去抢锁
            if (!hasQueuedPredecessor() && compareAndSetState(0, acquires)) {
                // 抢锁成功了
                this.exclusiveOwnerThread = Thread.currentThread();
                return true;
            }
        } else if (Thread.currentThread() == this.exclusiveOwnerThread) {
            // 重入 进入这里的if块是不存在并发的
            int nextc = c + acquires;
            if (nextc < 0) throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }

    /**
     * 返回当前线程前面是否有等待者线程
     * 返回false的情况 - 1.当前队列为空 2.当前线程为head.next线程，head.next在任何时候都可以去争取一下lock
     *
     * @return true 有 false 没有
     */
    private boolean hasQueuedPredecessor() {
        Node h = head;
        Node t = tail;
        Node s;
        // 1.h != t 说明当前队列已经有数据了
        // 2.(s = h.next) == null 是第一个获取锁失败的线程，会先帮助抢占到锁的线程创建head节点，在自己自旋入队的过程中的极端情况
        // 这种情况其实就是表示head后面已经有节点了
        // 3.s.thread != Thread.currentThread()  判断下当前线程是不是head.next，因为head.next是可以去竞争锁的

        return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
    }


    /**
     * 释放锁
     */
    @Override
    public void unlock() {
        release(1);
    }

    private void release(int arg) {
        if (tryRelease(arg)) {
            // 完全释放锁后，要去唤醒等待线程
            Node head = this.head;
            if (head.next != null) {
                // 公平锁唤醒head.next
                unparkSuccessor(head);
            }
        }
    }

    private void unparkSuccessor(Node node) {
        Node s = node.next;
        if (s != null && s.thread != null) {
            LockSupport.unpark(s.thread);
        }
    }

    /**
     * 完全释放锁返回true，否则说state>0，返回false
     */
    private boolean tryRelease(int arg) {
        if (this.exclusiveOwnerThread != Thread.currentThread()) {
            throw new RuntimeException("当前线程没有持有锁，无法释放锁");
        }
        // 这里是不存在并发的
        this.state = getState() - arg;
        if (this.state == 0) {
            // 完全释放锁
            // 1.exclusiveOwnerThread置空 2.state设置为0
            this.exclusiveOwnerThread = null;
            return true;
        }
        return false;
    }


    private void setHead(Node node) {
        this.head = node;
        // 当前node中的线程已经是获取锁的线程了
        node.thread = null;
        // help gc
        node.prev = null;
    }


    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }


}
