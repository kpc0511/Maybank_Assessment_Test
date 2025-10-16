package com.maybank.platform.services.restapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class PerformanceLoggerAspect {
    @Around("@annotation(com.maybank.platform.services.restapi.annotation.EnablePerformanceLogger)")
    public Object aroundEnablePerformanceLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        log.info("Performance Logger [{}.{}] - Start Timer", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();
        log.info("Performance Logger [{}.{}] - Time Used [{}ms]", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis());
        return proceed;
    }
}
