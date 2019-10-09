package com.zgu.springboot.util.limitflow;

/**
 * 获取流量通信证的结果，结果有3种：获取成功，获取失败，不需要获取
 */
public class AcquireResult {
    /**
     * 通行证获取成功
     */
    public static final int ACQUIRE_SUCCESS = 0;
    /**
     * 通行证获取失败
     */
    public static final int ACQUIRE_FAILED = 1;
    /**
     * 不需要获取通行证
     */
    public static final int ACQUIRE_NONEED = 2;
    /**
     * 获取通行证结果
     */
    private int result;
    /**
     * 可用通行证数量
     */
    private int availablePermits;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getAvailablePermits() {
        return availablePermits;
    }

    public void setAvailablePermits(int availablePermits) {
        this.availablePermits = availablePermits;
    }
}
