package com.example.demo1.util;

public class UserKey extends AbstractPrefix{
    public static final String COOKIE_NAME_TOKEN = "token";
    public static final int TOKEN_EXPIRE = 1800;

    public UserKey(int expireSeconds, String prefix){
        super(expireSeconds, prefix);
    }

    public static UserKey token = new UserKey(TOKEN_EXPIRE, "token");
    public static UserKey verifyCode = new UserKey(TOKEN_EXPIRE, "verifyCode");
    public static UserKey getById = new UserKey(TOKEN_EXPIRE, "getById");
}
