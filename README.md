# spring-rate-limiter
基于spring，SPEL的令牌桶限流 标签<br />
1. 暂时实现了实现了guavaRateLimiter<br />
2. V2将参考Spring Cloud Gateway的redis lua限流实现redisRateLimiter<br />


调用示例如下：

<pre><code>
public class TestService {
    @RateLimiter(
            name = "guavaRateLimiter",  //限流实现适配类
            key="'key_'+ #param1" ,     //key, 支持SPEL传参
            replenishRate=1L,           //每秒钟往桶内添加令牌的数量
            burstCapacity=100L,         //桶容量
            fallback="fallbackMethod")  //不允许提交时給用户返回的值，灵感来自spring cloud ribbon 
    public void testLimit(String param1){
        log.info("no limit!!");
    }
    
    //fallback调用
    public void fallbackMethod(){
    }
}
</code></pre>
