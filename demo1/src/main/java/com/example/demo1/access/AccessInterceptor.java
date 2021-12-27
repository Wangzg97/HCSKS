package com.example.demo1.access;

import com.example.demo1.common.CodeMsg;
import com.example.demo1.common.Result;
import com.example.demo1.controller.UserController;
import com.example.demo1.domain.UserInf;
import com.example.demo1.util.AccessKey;
import com.example.demo1.util.CookieUtil;
import com.example.demo1.util.RedisUtil;
import com.example.demo1.util.UserKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class AccessInterceptor implements HandlerInterceptor {
    private final UserController userController;
    @Autowired
    RedisUtil redisUtil;

    public AccessInterceptor(UserController userController, RedisUtil redisUtil){
        this.userController = userController;
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //获取或创建分布式SessionID
        CookieUtil.getSessionId(request, response);

        //将user保存到UserContext的ThreadLocal中
        UserInf user = getUser(request, response);
        UserContext.setUser(user);

        //处理自定义权限注解@AccessLimit
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //获取被调用方法上的AccessLimit注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            //如果没有该注解则放行
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            //needLogin
            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
            }
            //seconds maxCount
            if (seconds>0 && maxCount>0) {
                key += "_"+user.getUserId();
                AccessKey ak = AccessKey.withExpire(seconds);
                Integer count = redisUtil.get(ak, key, Integer.class);
                if(count == null) { // 未调用过
                    redisUtil.set(ak, key, 1);
                }else if(count < maxCount) { // 调用次数未达到上限
                    redisUtil.incr(ak, key);
                }else {
                    render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                    return false;
                }
            }
        }

        return true;
    }

    //从redis中获取User
    private UserInf getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserKey.COOKIE_NAME_TOKEN);
        String cookieToken = CookieUtil.getCookieValue(request, UserKey.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        return userController.getByToken(response, token);
    }

    //根据codeMsg生成错误响应
    private void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        //将CodeMsg包装成Result对象
        String str = RedisUtil.beanToString(Result.error(cm));
        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
