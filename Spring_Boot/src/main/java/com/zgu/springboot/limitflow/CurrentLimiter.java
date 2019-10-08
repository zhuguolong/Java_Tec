package com.zgu.springboot.limitflow;

import java.util.Vector;

/**
 * 限流器
 */
public class CurrentLimiter {
    /**
     * 本地线程变量，存储一次请求获取到的通行证，和其他并发请求隔离开，在controller执行完后释放本次请求获得的通行证
     */
    private static ThreadLocal<Vector<LimitRule>> localAcquiredLimitRules = new ThreadLocal<Vector<LimitRule>>();

    /**
     * 所有限流规则
     */
    private static Vector<LimitRule> allLimitRules = new Vector<LimitRule>();

    private CurrentLimiter() {};

    /**
     * 添加限流规则，在spring启动时添加不需要加锁，如果在运行状态添加，需要加锁
     * @param limitRule
     */
    public static void addRule(LimitRule limitRule) {
        printRule(limitRule);
        allLimitRules.add(limitRule);
    }

    /**
     * 获取流量通信证，所有流量规则都要获取后才能通过，如果一个不能获取则抛出异常
     * 多线程并发，需要加锁
     * @param urlPath
     * @throws Exception
     */
    public synchronized static void tryAcquire(String urlPath) throws Exception {
        // 有限流规则则处理
        if (allLimitRules.size() > 0) {
            // 能获取到通行证的流量规则要保存下来，在Controller执行完后要释放
            Vector<LimitRule> acquireLimitRules = new Vector<LimitRule>();
            for (LimitRule limitRule : allLimitRules) {
                // 获取通行证
                AcquireResult acquireResult = limitRule.tryAcquire(urlPath);
                if (AcquireResult.ACQUIRE_SUCCESS == acquireResult.getResult()) {
                    acquireLimitRules.add(limitRule);
                    // 获取到通行证的流量规则添加到本地线程变量
                    localAcquiredLimitRules.set(acquireLimitRules);
                } else if (AcquireResult.ACQUIRE_FAILED == acquireResult.getResult()) {
                    //如果获取不到通行证则抛出异常
                    StringBuilder builder = new StringBuilder("The request [")
                            .append(urlPath).append("] exceeds maximum traffic limit, the limit is ")
                            .append(limitRule.getMaxConcurrent())
                            .append(", available permit is")
                            .append(acquireResult.getAvailablePermits())
                            .append(".");

                    System.out.println(builder);
                    throw new Exception(builder.toString());
                } else {
                    StringBuilder builder = new StringBuilder("This path does not match the limit rule, path is [")
                            .append(urlPath)
                            .append("], pattern is [")
                            .append(limitRule.getPattern())
                            .append("].");
                    System.out.println(builder.toString());
                }
            }
        }
    }

    public synchronized static void release() {
        Vector<LimitRule> acquireLimitRules = localAcquiredLimitRules.get();
        if (null != acquireLimitRules && acquireLimitRules.size() > 0) {
            acquireLimitRules.forEach(limitRule -> {
                limitRule.release();
            });
        }

        // destory本地线程变量，避免内存泄漏
        localAcquiredLimitRules.remove();
    }

    /**
     * 打印限流规则
     * @param limitRule
     */
    private static void printRule(LimitRule limitRule) {
        StringBuilder builder = new StringBuilder("Add Limit Rule, Max Concurrent: ")
                .append(limitRule.getMaxConcurrent())
                .append(", Pattern: ")
                .append(limitRule.getPattern());
        System.out.println(builder.toString());
    }
}
