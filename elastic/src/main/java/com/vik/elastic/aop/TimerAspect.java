package com.vik.elastic.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class TimerAspect {

	@Around("@annotation(com.vik.elastic.aop.TimerAnnotation)")
	public Object logTimer(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = joinPoint.proceed();
		long executionTime = System.currentTimeMillis() - start;
		log.info("{} executed in {} millis", joinPoint.getSignature(), executionTime);
		return proceed;
	}
}