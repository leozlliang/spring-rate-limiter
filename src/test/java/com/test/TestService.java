package com.test;

import com.spring.limiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/11.
 */
@Slf4j
public class TestService {

    @RateLimiter(
            name = "guavaRateLimiter",
            key="'key_'+ #param1" ,
            replenishRate=1L,
            burstCapacity=100L )
    public void testLimit(String param1){
        log.info("no limit!!");
    }
}
