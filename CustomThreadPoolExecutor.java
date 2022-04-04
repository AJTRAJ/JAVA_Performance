package com.by.practice.mdap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, AbortPolicy abortPolicy) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

	/*
	 * public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int
	 * keepAliveTime, TimeUnit seconds, BlockingQueue<Runnable> blockingQueue,
	 * AbortPolicy abortPolicy) { // TODO Auto-generated constructor stub }
	 */

	@Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("Perform beforeExecute() logic");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            System.out.println("Perform exception handler logic");
        }
        System.out.println("Perform afterExecute() logic");
    }
}