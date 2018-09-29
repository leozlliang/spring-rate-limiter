# spring-rate-limiter
基于spring，SPEL的令牌桶限流 标签<br />
1. 暂时实现了实现了guavaRateLimiter<br />
2. V2将参考Spring Cloud Gateway的redis lua限流实现redisRateLimiter<br />


调用示例如下：

<pre><code>
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
</code></pre>
