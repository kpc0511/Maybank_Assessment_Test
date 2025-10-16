package com.maybank.platform.services.restapi.aspect;

import com.maybank.platform.services.restapi.annotation.RedisQuery;
import com.maybank.platform.services.restapi.annotation.RedisUpdate;
import com.maybank.platform.services.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class RedisAspect {
    @Resource
    RedisUtil redisUtil;

    @Around("@annotation(com.maybank.platform.services.restapi.annotation.RedisQuery)")
    public Object proceedRedisAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Object[] args = proceedingJoinPoint.getArgs();
        RedisQuery redis = method.getAnnotation(RedisQuery.class);

        if (redis.parameters().length > 0 && Arrays.stream(redis.parameters()).anyMatch(i -> i >= method.getParameterCount())) {
            return proceedingJoinPoint.proceed(args);
        } else {
            String key = getRedisKey(redis.key(), redis.parameters(), args);
            Object object;

            if (!redisUtil.exists(key)) {
                object = proceedingJoinPoint.proceed(args);

                if (ObjectUtils.isEmpty(object)) {
                    if (redis.activeTime() > 0) {
                        redisUtil.put(key, object, Math.round(redis.activeTime() / 2));
                    } else {
                        redisUtil.put(key, object);
                    }
                } else {
                    if (redis.activeTime() > 0) {
                        redisUtil.put(key, object, redis.activeTime());
                    } else {
                        redisUtil.put(key, object, 0);
                    }
                }
            } else {
                object = redisUtil.get(key, method.getReturnType());
            }

            return object;
        }
    }

    @AfterReturning(pointcut="@annotation(com.maybank.platform.services.restapi.annotation.RedisUpdate)", returning="result")
    public void proceedRedisUpdateAnnotation(JoinPoint joinPoint, Object result) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        RedisUpdate redis = method.getAnnotation(RedisUpdate.class);
        boolean success = (result instanceof Boolean && result.equals(true) || result != null);

        if (success && (redis.parameters().length <= 0 || Arrays.stream(redis.parameters()).noneMatch(i -> i >= method.getParameterCount()))) {
            String key = getRedisKey(redis.key(), redis.parameters(), args);
            redisUtil.delete(key);
        }
    }

    private String getRedisKey(String key, int[] parameters, Object[] args) {
        if (parameters.length > 0) {
            Object[] objects = Arrays.stream(parameters).mapToObj(i -> args[i]).collect(Collectors.toList()).toArray();
            return MessageFormat.format(key, objects);
        } else {
            return key;
        }
    }
}
