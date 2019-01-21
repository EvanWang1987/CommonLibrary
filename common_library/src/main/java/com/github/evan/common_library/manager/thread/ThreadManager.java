package com.github.evan.common_library.manager.thread;

import com.github.evan.common_library.utils.DeviceUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Evan on 2017/12/10.
 */

public class ThreadManager {
    private static ThreadManager mInstance = null;
    public static ThreadManager getInstance(){
        if(null == mInstance){
            synchronized (ThreadManager.class){
                if(null == mInstance){
                    mInstance = new ThreadManager();
                }
            }
        }
        return mInstance;
    }

    private static final String OTHER_POOL = "OTHER_POOL";
    private static final String NET_WORK_POOL = "NET_WORK_POOL";
    private static final String IO_POOL = "IO_POOL";
    private static final String SINGLE_POOL = "SINGLE_POOL";
    private static final String QR_POOL = "QR_POOL";

    private Map<String, ThreadPoolExecutor> mPoolExecutors = new HashMap<>();

    private ThreadManager(){
        int numberOfCPUCores = DeviceUtil.getNumberOfCPUCores();
        ThreadPoolExecutor otherPool = new ThreadPoolExecutor(3, 6, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory("Other Thread Pool"), new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor netWorkPool = new ThreadPoolExecutor(5, 10, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory("Network Thread Pool"), new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor ioPool = new ThreadPoolExecutor(numberOfCPUCores * 2, numberOfCPUCores * 4, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory("IO Thread Pool"), new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor singlePool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory("Single Thread Pool"), new ThreadPoolExecutor.AbortPolicy());
        ThreadPoolExecutor qrPool = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory("QR Scan Thread Pool"), new ThreadPoolExecutor.AbortPolicy());

        otherPool.prestartCoreThread();         //预创建1个线程
        netWorkPool.prestartAllCoreThreads();   //预创建所有线程
        ioPool.prestartAllCoreThreads();
        singlePool.prestartCoreThread();
        qrPool.prestartCoreThread();

        mPoolExecutors.put(OTHER_POOL, otherPool);
        mPoolExecutors.put(NET_WORK_POOL, netWorkPool);
        mPoolExecutors.put(IO_POOL, ioPool);
        mPoolExecutors.put(SINGLE_POOL, singlePool);
        mPoolExecutors.put(QR_POOL, qrPool);
    }

    public void shutDown(){
        Set<Map.Entry<String, ThreadPoolExecutor>> entries = mPoolExecutors.entrySet();
        Iterator<Map.Entry<String, ThreadPoolExecutor>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ThreadPoolExecutor> next = iterator.next();
            ThreadPoolExecutor value = next.getValue();
            value.shutdown();
        }
    }

    public void shutDownNow(){
        Set<Map.Entry<String, ThreadPoolExecutor>> entries = mPoolExecutors.entrySet();
        Iterator<Map.Entry<String, ThreadPoolExecutor>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ThreadPoolExecutor> next = iterator.next();
            ThreadPoolExecutor value = next.getValue();
            value.shutdownNow();
        }
    }

    public ThreadPoolExecutor getIOThreadPool(){
        return mPoolExecutors.get(IO_POOL);
    }

    public ThreadPoolExecutor getSingleThreadPool(){
        return mPoolExecutors.get(SINGLE_POOL);
    }

    public ThreadPoolExecutor getNetworkThreadPool(){
        return mPoolExecutors.get(NET_WORK_POOL);
    }

    public ThreadPoolExecutor getOtherThreadPool(){
        return mPoolExecutors.get(OTHER_POOL);
    }

    public ThreadPoolExecutor getQrScanThreadPool() {
        return mPoolExecutors.get(QR_POOL);
    }
}
