package com.geek.framework.processor.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * Web 接口日志切面
 *
 * 对 com.geek 包下所有 *Controller 公共方法，统一打印请求地址、入参和返回结果。
 */
@Aspect
@Component
public class WebLogAspect {

    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("WebLog-CostTime");

    /**
     * 所有 Controller 公共方法
     */
    @Pointcut("execution(public * com.geek..*Controller.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
        WebLogUtil.requestLog(joinPoint);
    }

    @AfterReturning(pointcut = "webLog()", returning = "ret")
    public void doAfterReturn(Object ret) {
        Long startTime = TIME_THREADLOCAL.get();
        if (startTime != null) {
            WebLogUtil.responseLog(ret, startTime);
            TIME_THREADLOCAL.remove();
        }
    }

    @AfterThrowing(pointcut = "webLog()", throwing = "ex")
    public void doAfterThrowing(Throwable ex) {
        Long startTime = TIME_THREADLOCAL.get();
        if (startTime != null) {
            WebLogUtil.throwableLog(ex, startTime);
            TIME_THREADLOCAL.remove();
        }
    }
}

