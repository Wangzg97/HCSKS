package com.example.demo1.util;

public abstract class AbstractPrefix implements KeyPrefix{
    private final int expireSeconds;
    private final String prefix;

    public AbstractPrefix(String prefix){
        //-1表示永不过期
        this(-1, prefix);
    }
    public AbstractPrefix(int expireSeconds, String prefix){
        //设置过期时间
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}
