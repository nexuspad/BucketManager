package com.np.bucketmanager.state;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimedCacheAspect {
    Logger logger = LoggerFactory.getLogger(TimedCacheAspect.class);

    @After("@annotation(TimedCacheCheck)")
    public void checkTimedCache(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        logger.info("Check expired cache in: {}.{}", className, methodName);

        if (joinPoint.getTarget() instanceof TimedCache) {
            ((TimedCache<?>) joinPoint.getTarget()).purgeExpired();
        }
    }
}
