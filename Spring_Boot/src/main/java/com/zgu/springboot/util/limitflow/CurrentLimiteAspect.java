package com.zgu.springboot.util.limitflow;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CurrentLimiteAspect {
    /**
     * 匹配所有以 Controller 类中的方法
     */
    @Pointcut("execution(* com.zgu.springboot..*Controller.*(..))")
    public void controller() {}

    @Before("controller()")
    public void beforeController(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        CurrentLimiter.tryAcquire(request.getRequestURI());
    }

    @After("controller()")
    public void afterController(JoinPoint joinPoint) {
        CurrentLimiter.release();
    }
}
