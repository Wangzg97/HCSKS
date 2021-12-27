package com.example.demo1.access;

import com.example.demo1.domain.UserInf;

public class UserContext {
    private static final ThreadLocal<UserInf> userholder = new ThreadLocal<>();
    public static void setUser(UserInf user) {
        userholder.set(user);
    }
    public static UserInf getUser() {
        return userholder.get();
    }
}
