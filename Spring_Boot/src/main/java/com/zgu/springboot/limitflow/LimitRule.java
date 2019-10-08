package com.zgu.springboot.limitflow;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 限流规则
 */
public class LimitRule {
    /**
     * 信号量
     */
    private final Semaphore sema;
    /**
     * 请求 url 匹配规则
     */
    private final String pattern;
    /**
     * 最大并发数
     */
    private final int maxConcurrent;

    public LimitRule(String pattern, int maxConcurrent) {
        this.sema = new Semaphore(maxConcurrent);
        this.pattern = pattern;
        this.maxConcurrent = maxConcurrent;
    }

    /**
     * 获取通行证
     * @param urlPath 请求 url
     * @return 0-获取成功，1-没有获取到通行证，2-不需要获取通行证
     */
    public synchronized AcquireResult tryAcquire(String urlPath) {
        AcquireResult acquireResult = new AcquireResult();
        acquireResult.setAvailablePermits(this.sema.availablePermits());

        try {
            if (Pattern.matches(pattern, urlPath)) {
                boolean acquire = this.sema.tryAcquire(50, TimeUnit.MILLISECONDS);
                if (acquire) {
                    acquireResult.setResult(AcquireResult.ACQUIRE_SUCCESS);
                    print(urlPath);
                } else {
                    acquireResult.setResult(AcquireResult.ACQUIRE_FAILED);
                }
                print(urlPath);
            } else {
                acquireResult.setResult(AcquireResult.ACQUIRE_NONEED);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return acquireResult;
    }

    /**
     * 释放通行证
     */
    public synchronized void release() {
        this.sema.release();
        print(null);
    }

    /**
     * 获取最大并发数
     * @return
     */
    public int getMaxConcurrent() {
        return this.maxConcurrent;
    }

    public String getPattern() {
        return this.pattern;
    }

    /**
     * 打印日志
     * @param urlPath
     */
    private void print(String urlPath) {
        StringBuilder builder = new StringBuilder("Pattern: ")
                .append(pattern)
                .append(", ");
        if (StringUtils.isNotBlank(urlPath)) {
            builder.append("urlPath: ")
                    .append(urlPath)
                    .append(", ");
        }
        builder.append("Available Permits: ")
                .append(this.sema.availablePermits());
        System.out.println(builder.toString());
    }
}
