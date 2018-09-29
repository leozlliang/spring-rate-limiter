package com.spring.limiter.support;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.revinate.guava.util.concurrent.RateLimiter;
import com.spring.limiter.IRateLimiter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/9/29.
 */
public class GuavaRateLimiter implements IRateLimiter {
    @Setter
    private String name;

    private Cache<String, RateLimiter> limiterMap= CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    public String getName() {
        return name;
    }

    public boolean isAllow(String key, long replenishRate, long burstCapacity) {
        RateLimiter limiter = limiterMap.getIfPresent(key);
        if(limiter==null){
            double maxBurstSeconds = BigDecimal.valueOf(burstCapacity).divide(BigDecimal.valueOf(replenishRate),2,BigDecimal.ROUND_HALF_UP).doubleValue();
            limiter = RateLimiter.create(replenishRate,maxBurstSeconds);
            limiterMap.put(key,limiter);
        }
        boolean isAllow = limiter.tryAcquire();
        return isAllow;
    }
}
