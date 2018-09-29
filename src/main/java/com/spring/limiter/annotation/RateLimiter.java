package com.spring.limiter.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/9/29.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RateLimiter {
    String name() default "";
    String key() default "";
    long replenishRate() default 1L;
    long burstCapacity() default 1L;
    String fallback() default  "";
}
