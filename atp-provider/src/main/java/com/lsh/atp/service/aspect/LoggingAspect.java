package com.lsh.atp.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * lsh-atp
 * Created by peter on 16/9/7.
 */
@Order(1)
@Aspect
@Component
public class LoggingAspect {

    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * 切入点表达式.
     */
    @Pointcut("execution(* com.lsh.atp.service.*.*RPCService*.*(..))")
    public void declareJointPointExpression(){}

    /**
     * 前置通知
     * @param joinPoint 连接点
     */
//    @Before("declareJointPointExpression()")
//    public void beforeMethod(JoinPoint joinPoint){
//        String methodName = joinPoint.getSignature().getName();
//        String thisclass = joinPoint.getTarget().getClass().getName();
//
//        logger.info("------>The class is: " + thisclass + " ; the method is : " + methodName);
//    }

    /**
     * 后置通知
     * @param joinPoint 连接点
     */
//    @After("declareJointPointExpression()")
//    public void afterMethod(JoinPoint joinPoint){
//        String methodName = joinPoint.getSignature().getName();
//        String thisclass = joinPoint.getTarget().getClass().getName();
//        logger.info("------>The class is: " + thisclass + " ; the method is : " + methodName + " ends");
//    }

    /**
     * 环绕通知
     * @param joinPoint 连接点
     * @throws Throwable 异常
     */
    @Around("declareJointPointExpression()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        //打印方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Object result = joinPoint.proceed();

        logger.info("方法名为 : " + method.getName() + ", runtime is : " + (System.currentTimeMillis() - startTime) +"毫秒");

        return result;

    }

}
