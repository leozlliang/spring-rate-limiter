package com.spring.limiter;

/**
 * Created by Administrator on 2018/9/29.
 */
public interface IRateLimiter {
    String getName();

    public boolean isAllow(String key,long replenishRate,long burstCapacity);


}
