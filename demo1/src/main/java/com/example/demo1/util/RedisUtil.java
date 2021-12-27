package com.example.demo1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public RedisUtil(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    //根据key获取value
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        String realKey = prefix.getPrefix()+key;
        //根据key获取value
        String str = redisTemplate.opsForValue().get(realKey);
        try{
            //将读取到的结果字符串转为T对象
            return stringToBean(str, clazz);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return null;
    }

    //添加key-value
    public <T> Boolean set(KeyPrefix prefix, String key, T value){
        String str = null;
        try{
            //将T对象转为字符串
            str = beanToString(value);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        if(str == null || str.length()<=0){
            return false;
        }
        //实际的key由prefix和key组成，且prefix决定了key的过期时间
        String realKey = prefix.getPrefix()+key;
        //获取过期时间
        int seconds = prefix.expireSeconds();
        //expireSeconds为过期时间，seconds<=0时永不过期
        if(seconds<=0){
            //永不过期时
            redisTemplate.opsForValue().set(realKey, str);
        }else{
            //设置过期时间
            redisTemplate.opsForValue().set(realKey, str, Duration.ofSeconds(seconds));
        }
        return true;
    }

    //判断指定key是否存在
    public Boolean exists(KeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.hasKey(realKey);
    }

    //根据key删除数据
    public Boolean delete(KeyPrefix prefix, String key){
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.delete(realKey);
    }

    //对指定key加1
    public Long incr(KeyPrefix prefix, String key){
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.opsForValue().increment(realKey);
    }

    //对指定key减1
    public Long decr(KeyPrefix prefix, String key){
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.opsForValue().decrement(realKey);
    }

    //字符串转为T对象
    public static <T> T stringToBean(String str, Class<T> clazz) throws JsonProcessingException {
        if(str==null || str.length()<=0 || clazz==null){
            return  null;
        }
        if(clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(str);
        }else if(clazz == Long.class || clazz == long.class){
            return (T) Long.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else{
            return objectMapper.readValue(str, clazz);
        }
    }

    //将T对象转为字符串
    public static <T> String beanToString(T value) throws JsonProcessingException {
        if (value==null){
            return "";
        }
        Class<?> aClass = value.getClass();
        if (aClass == Integer.class || aClass == Long.class){
            return ""+value;
        }else if(aClass == String.class){
            return (String)value;
        }else {
            return objectMapper.writeValueAsString(value);
        }
    }
}
