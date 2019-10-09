package com.zgu.springboot.util.concurrent;

public class DiscardRejectPolicy implements RejectPolicy {
    @Override
    public void reject(Runnable task, MyThreadPoolExecutor myThreadPoolExecutor) {
        // TODO do nothing
        System.out.println("discard one task");
    }
}
