package com.spring.limiter.support;

import com.spring.limiter.IRateLimiter;

import java.util.Collection;

/**
 * Created by Administrator on 2018/7/11.
 */
public class SimpleRateLimiterManager extends AbstractRateLimiterManager {
    private Collection<? extends IRateLimiter> limiters;

    public SimpleRateLimiterManager() {
    }

    public void setLimiters(Collection<? extends IRateLimiter> limiters) {
        this.limiters = limiters;
    }

    protected Collection<? extends IRateLimiter> loadLimiters() {
        return this.limiters;
    }
}
