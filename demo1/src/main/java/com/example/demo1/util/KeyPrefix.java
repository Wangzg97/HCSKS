package com.example.demo1.util;

public interface KeyPrefix {
    //控制过期时间
    int expireSeconds();
    //键的前缀
    String getPrefix();
}
