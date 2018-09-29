package com.spring.limiter;

import java.util.Collection;

/**
 * Created by Administrator on 2018/9/29.
 */
public interface RateLimiterManager {
    public IRateLimiter getRateLimiter(String name);
    public Collection<String> getLimiterNames();
}

