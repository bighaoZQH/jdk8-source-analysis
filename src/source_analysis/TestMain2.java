package source_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: bighao周启豪
 * @Date: 2020/3/3 21:40
 * @Version 1.0
 */
public class TestMain2 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * JDK源码分析
         * 目前看过的：
         * 集合：
         *  HashMap
         *  ThreadLocal
         *  ArrayList
         *
         * JUC:
         *  ReentrantLock
         *  ReentrantReadWriteLock
         *  LongAdder
         *  FutureTask
         *
         */
        HashMap hashMap = new HashMap();
        hashMap.put("a", "a");
        hashMap.get("a");
        hashMap.remove("a");

        LongAdder longAdder = new LongAdder();
        longAdder.add(1);

        ConcurrentHashMap map = new ConcurrentHashMap();
        map.put("a", "a");


        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(5);
        arrayList.add("a");

    }
}
