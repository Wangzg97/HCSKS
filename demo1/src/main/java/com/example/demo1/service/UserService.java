package com.example.demo1.service;

import com.example.demo1.common.CodeMsg;
import com.example.demo1.common.exception.MiaoshaException;
import com.example.demo1.common.vo.LoginVo;
import com.example.demo1.domain.UserInf;
import com.example.demo1.mapper.UserInfMapper;
import com.example.demo1.util.MD5Util;
import com.example.demo1.util.RedisUtil;
import com.example.demo1.util.UserKey;
import com.example.demo1.util.VercodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    UserInfMapper userInfMapper;
    @Autowired
    RedisUtil redisUtil;

    //创建图形验证码
    public BufferedImage createVerifyCode(String token) {
        if (token == null) {
            return null;
        }
        Random random = new Random();
        String verifyCode = VercodeUtil.generateVerifyCode(random);
        int ret = VercodeUtil.calc(verifyCode);
        redisUtil.set(UserKey.verifyCode, token, ret);

        return VercodeUtil.createVerifyImage(verifyCode, random);
    }

    //验证验证码正确性
    public Boolean checkVerifyCode(String token, int verifyCode) {
        if (token == null) {
            return false;
        }
        Integer correctCode = redisUtil.get(UserKey.verifyCode, token, Integer.class);
        if (correctCode == null || correctCode-verifyCode!=0) {
            return false;
        }
        redisUtil.delete(UserKey.verifyCode, token);
        return true;
    }

    //处理用户登录
    public UserInf login(LoginVo loginVo) {
        if (loginVo == null) {
            throw new MiaoshaException((CodeMsg.SERVER_ERROR));
        }
        String mobile = loginVo.getMobile();
        UserInf user = userInfMapper.findByUserId(Long.parseLong(mobile));
        //用户不存在
        if(user == null) {
            throw new MiaoshaException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String password = user.getPassword();
        String calcPass = MD5Util.passToDbPass(loginVo.getPassword(), user.getSalt());
        //密码错误
        if (!password.equals(calcPass)){
            throw new MiaoshaException(CodeMsg.PASSWORD_ERROR);
        }
        //登录次数刷新
        user.setLoginCount(user.getLoginCount()+1);
        //更新最后登录时间
        user.setLastLoginDate(new Date());
        //更新用户信息
        userInfMapper.updateUserInf(user);

        return user;
    }

    //从redis/数据库中读取用户
    private UserInf getUserById(long id) {
        UserInf userInf = redisUtil.get(UserKey.getById, ""+id, UserInf.class);
        if(userInf != null) {
            return userInf;
        }
        userInf = userInfMapper.findByUserId(id);
        if (userInf != null) {
            //将读取到的用户信息存入redis
            redisUtil.set(UserKey.getById, ""+id, userInf);
        }
        return userInf;
    }
}
