package com.zgu.springboot.util.concurrent;

public interface RejectPolicy {
    void reject(Runnable task, MyThreadPoolExecutor myThreadPoolExecutor);
}
