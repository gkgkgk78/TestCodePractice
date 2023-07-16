package com.springboot.test.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//어노테이션이 runtime까지 유지되도록 설정한다.
public @interface LogExecutionTime {


}
