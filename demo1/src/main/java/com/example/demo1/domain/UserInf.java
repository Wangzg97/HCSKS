package com.example.demo1.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀用户表
 * @TableName user_inf
 */
@Data
public class UserInf implements Serializable {
    /**
     * 手机号作为id
     */
    private Long userId;

    /**
     * 
     */
    private String nickname;

    /**
     * 保存加盐加密后的密码：MD5（密码，salt）
     */
    private String password;

    /**
     * 
     */
    private String salt;

    /**
     * 头像地址
     */
    private String head;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 上次登录时间
     */
    private Date lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;

    private static final long serialVersionUID = 1L;
}