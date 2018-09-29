package com.spring.limiter.support;

import com.spring.limiter.IRateLimiter;
import com.spring.limiter.RateLimiterManager;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public abstract class AbstractRateLimiterManager implements RateLimiterManager, InitializingBean {
    private final ConcurrentMap<String, IRateLimiter> limiterMap = new ConcurrentHashMap(16);
    private volatile Set<String> limiterNames = Collections.emptySet();

    public AbstractRateLimiterManager() {
    }

    public void afterPropertiesSet() {
        this.initializeLimiters();
    }

    public void initializeLimiters() {
        Collection limiters = this.loadLimiters();
        ConcurrentMap var2 = this.limiterMap;
        synchronized(this.limiterMap) {
            this.limiterNames = Collections.emptySet();
            this.limiterNames.clear();
            LinkedHashSet limiterNameSet = new LinkedHashSet(limiters.size());
            Iterator var4 = limiters.iterator();

            while(var4.hasNext()) {
                IRateLimiter limiter = (IRateLimiter)var4.next();
                String name = limiter.getName();
                this.limiterMap.put(name, limiter);
                limiterNameSet.add(name);
            }

            this.limiterNames = Collections.unmodifiableSet(limiterNameSet);
        }
    }

    protected abstract Collection<? extends IRateLimiter> loadLimiters();

    public IRateLimiter getRateLimiter(String name) {
        IRateLimiter limiter = (IRateLimiter)this.limiterMap.get(name);
        if(limiter != null) {
            return limiter;
        } else {
            ConcurrentMap var3 = this.limiterMap;
            synchronized(this.limiterMap) {
                limiter = this.limiterMap.get(name);
                if(limiter == null) {
                    limiter = this.getMissingLimiter(name);
                    if(limiter != null) {
                        this.limiterMap.put(name, limiter);
                        this.updateLimiterNames(name);
                    }
                }

                return limiter;
            }
        }
    }

    public Collection<String> getLimiterNames() {
        return this.limiterNames;
    }

    protected final IRateLimiter lookupLimiter(String name) {
        return (IRateLimiter)this.limiterMap.get(name);
    }

    protected final void addLimiter(IRateLimiter limiter) {
        String name = limiter.getName();
        ConcurrentMap var3 = this.limiterMap;
        synchronized(this.limiterMap) {
            if(this.limiterMap.put(name, limiter) == null) {
                this.updateLimiterNames(name);
            }

        }
    }

    private void updateLimiterNames(String name) {
        LinkedHashSet limiterNames = new LinkedHashSet(this.limiterNames.size() + 1);
        limiterNames.addAll(this.limiterNames);
        limiterNames.add(name);
        this.limiterNames = Collections.unmodifiableSet(limiterNames);
    }


    protected IRateLimiter getMissingLimiter(String name) {
        return null;
    }
}
