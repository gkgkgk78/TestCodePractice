package com.springboot.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class LogAspect {

    Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(LogExecutionTime)")
    //명칭된 어노테이션을 사용하는 메소드는 proceedingjoinpoint 파라미터를 받음

    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // @LogExecutionTime 애노테이션이 붙어있는 타겟 메소드를 실행
        Object proceed = joinPoint.proceed();

        stopWatch.stop();
        logger.info("start");
        logger.info("checked "+stopWatch.prettyPrint());

        return proceed; // 결과 리턴
    }


}
