package com.lsh.atp.core.service.email;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;


/**
 * @ClassName: ThreadPoolSet
 * @Description: 发送邮件线程池
 * @date : 2016年10月17日
 * @author： peter
 * @version 1.0
 */
public class ThreadPoolSet {

    private HashMap<String,ExecutorService> poolSet;
    private static ThreadPoolSet instance = null;

    /**
     * 构建器
     */
    private ThreadPoolSet() {
        poolSet = new HashMap<String,ExecutorService>();
    }

    /**
     * 取得线程池的集合的实例
     * @return ThreadPoolSet
     */
    public static synchronized ThreadPoolSet getInstance() {
        if (instance == null) {
            instance = new ThreadPoolSet();
        }
        return instance;
    }

    /**
     * 通过名字取得集合中线程池
     * @param poolName String 线程池的名字
     * @return ThreadPool
     */
    public ExecutorService getThreadPool(String poolName) {
        return poolSet.get(poolName);
    }

    /**
     * 添加线程池到集合
     * @param poolName String 线程池的名称
     * @param threadPool ThreadPool 线程池的实例
     */
    public void addThreadPool(String poolName, ExecutorService threadPool) {
        poolSet.put(poolName, threadPool);
    }

}
