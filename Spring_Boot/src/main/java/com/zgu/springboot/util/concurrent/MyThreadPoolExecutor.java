package com.zgu.springboot.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutor implements Executor {
    /**
     * 线程池名称
     */
    private String name;
    /**
     * 线程序列号
     */
    private AtomicInteger sequence = new AtomicInteger(0);
    /**
     * 核心线程数
     */
    private int coreSize;
    /**
     * 最大线程数
     */
    private int maxSize;
    /**
     * 任务队列
     */
    private BlockingQueue<Runnable> taskQueue;
    /**
     * 拒绝策略
     */
    private RejectPolicy rejectPolicy;
    /**
     * 当正在运行的线程数需要修改时线程间立即感知，所以使用
     * AtomicInteger
     */
    private AtomicInteger runningCount = new AtomicInteger(0);

    public MyThreadPoolExecutor(String name, int coreSize, int maxSize, BlockingQueue<Runnable> taskQueue, RejectPolicy rejectPolicy) {
        this.name = name;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.taskQueue = taskQueue;
        this.rejectPolicy = rejectPolicy;
    }

    @Override
    public void execute(Runnable task) {
        // 正在运行的线程数
        int count = runningCount.get();
        if (count < coreSize) {
            if (!addWorker(task, true)) {
                return;
            }
        }

        if (taskQueue.offer(task)) {
            // TODO do others things
        } else {
            if (!addWorker(task, false)) {
                // 如果添加非核心线程失败，则执行拒绝策略
                rejectPolicy.reject(task, this);
            }
        }
    }

    private boolean addWorker(Runnable newTask, boolean core) {
        // 自旋判断是不是真的可以创建一个线程
        for (;;) {
            // 正在运行的线程数
            int count = runningCount.get();
            // 核心线程还是非核心线程
            int max = core ? coreSize : maxSize;
            // 不满足创建线程的条件，直接返回false
            if (count >= max) {
                return false;
            }
            // 修改runningCount成功，可以创建线程
            if (runningCount.compareAndSet(count, count + 1)) {
                // 线程名字
                String threadName = (core ? "core_" : "") + name + sequence.incrementAndGet();
                // 创建线程并启动
                new Thread(() -> {
                    System.out.println("thread name: " + Thread.currentThread().getName());
                    // 运行的任务
                    Runnable task = newTask;
                    while (null != task || (null != (task = getTask()))) {
                        try {
                            // 执行任务
                            task.run();
                        } finally {
                            // 置空
                            task = null;
                        }
                    }
                }, threadName).start();
            }
        }
    }

    private Runnable getTask() {
        try {
            // take() 方法会一直阻塞直到渠道任务为止
            return taskQueue.take();
        } catch (InterruptedException e) {
            // 当线程中断，返回null可以结束当前线程
            // 当线程结束了，吧runningCount值减1
            runningCount.decrementAndGet();
            return null;
        }
    }
}
