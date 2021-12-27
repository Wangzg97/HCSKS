package com.example.demo1.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    //被修饰的方法是否需登录才能使用
    boolean needLogin() default true;
    //限制被修饰的方法在指定时间内最多调用几次,-1表示不限制
    int seconds() default -1;
    int maxCount() default -1;
}
