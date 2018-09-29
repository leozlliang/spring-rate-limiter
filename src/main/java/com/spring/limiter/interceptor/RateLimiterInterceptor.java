package com.spring.limiter.interceptor;

import com.spring.limiter.IRateLimiter;
import com.spring.limiter.RateLimiterManager;
import com.spring.limiter.annotation.RateLimiter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import utils.ReflectExtUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/9/29.
 */
@Slf4j
@Component
public class RateLimiterInterceptor implements MethodInterceptor {
    @Autowired
    private RateLimiterManager rateLimiterManager;
    private ExpressionParser parser = new SpelExpressionParser();


    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        RateLimiter rateLimiterAnn = AnnotationUtils.findAnnotation(methodInvocation.getMethod(),RateLimiter.class);
        if(rateLimiterAnn==null){
            return methodInvocation.proceed();
        }
        IRateLimiter rateLimiter =  rateLimiterManager.getRateLimiter(rateLimiterAnn.name());
        if(rateLimiter==null){
            return methodInvocation.proceed();
        }
        String[] argNames = ReflectExtUtils.getParamNames(methodInvocation.getMethod());
        String key = getKeyFromSpel(argNames,methodInvocation.getArguments(),rateLimiterAnn.key());
        if(StringUtils.isBlank(key)){
            return methodInvocation.proceed();
        }
        boolean isAllow = rateLimiter.isAllow(key,rateLimiterAnn.replenishRate(),rateLimiterAnn.burstCapacity());
        if(isAllow==false){
            return null;
        }
        return methodInvocation.proceed();
    }

    private String getKeyFromSpel(String[] argNames, Object[] args, String spel){

        StandardEvaluationContext context = new StandardEvaluationContext();
        for(int i=0;i<argNames.length;i++){
            context.setVariable(argNames[i],args[i]);
        }
        Object key = parser.parseExpression(spel).getValue(context);
        return key.toString();
    }
}
