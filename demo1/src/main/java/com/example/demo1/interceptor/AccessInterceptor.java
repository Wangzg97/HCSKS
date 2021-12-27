package com.example.demo1.interceptor;

import com.example.demo1.controller.UserController;
import com.example.demo1.util.CookieUtil;
import com.example.demo1.util.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessInterceptor implements HandlerInterceptor {
    private final UserController userController;
    private final RedisUtil redisUtil;

    public AccessInterceptor(UserController userController, RedisUtil redisUtil){
        this.userController = userController;
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //获取或创建分布式SessionID
        CookieUtil.getSessionId(request, response);

        return true;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }
}
