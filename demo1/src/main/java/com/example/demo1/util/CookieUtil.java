package com.example.demo1.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class CookieUtil {
    //将sessionID以cookie形式写入浏览器
    public static void addSessionId(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(UserKey.COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //读取cookie
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }

    //通过Cookie读取分布式SessionID，如果不存在则创建新的分布式SessionID
    public static String getSessionId(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.getCookieValue(request, UserKey.COOKIE_NAME_TOKEN);
        if(token == null) {
            token = UUID.randomUUID().toString();
            addSessionId(response, token);
        }
        return token;
    }
}
