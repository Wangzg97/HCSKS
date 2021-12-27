package com.example.demo1.controller;

import com.example.demo1.common.CodeMsg;
import com.example.demo1.common.Result;
import com.example.demo1.common.exception.MiaoshaException;
import com.example.demo1.common.vo.LoginVo;
import com.example.demo1.domain.UserInf;
import com.example.demo1.service.UserService;
import com.example.demo1.util.CookieUtil;
import com.example.demo1.util.MD5Util;
import com.example.demo1.util.RedisUtil;
import com.example.demo1.util.UserKey;
import com.sun.net.httpserver.Authenticator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    @GetMapping("/verifyCode")
    public void getLoginVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //从cookie获取分布式sessionID
        String token = CookieUtil.getSessionId(request, response);
        //创建验证码照片
        BufferedImage image = userService.createVerifyCode(token);
        OutputStream out = response.getOutputStream();
        //输出验证码
        ImageIO.write(image, "JPEG", out);
        out.flush();
        out.close();
    }
    @PostMapping("proLogin")
    @ResponseBody
    public Result<Boolean> proLogin(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {
        //从cookie获取分布式sessionID
        String token = CookieUtil.getSessionId(request, response);
        if (token!=null) {
            //验证码错误
            if (userService.checkVerifyCode(token, loginVo.getVercode())){
                return Result.error(CodeMsg.REQUEST_ILLEGAL);
            }
            UserInf userInf = getByToken(response, token);
            if(userInf!=null && userInf.getUserId().toString().equals(loginVo.getMobile()) && MD5Util.passToDbPass(loginVo.getPassword(), userInf.getSalt()).equals(userInf.getPassword())) {
                return Result.success(true);
            }
        }
        try{
            UserInf userInf = userService.login(loginVo);
            addSession(response, token, userInf);
            return Result.success(true);
        }catch (MiaoshaException e){
            return Result.error(e.getCodeMsg());
        }
    }

    //使用redis缓存实现保存分布式session
    private void addSession(HttpServletResponse response, String token, UserInf user){
        //redis缓存保存分布式session信息
        redisUtil.set(UserKey.token, token, user);
        //Cookie保存分布式SessionID
        CookieUtil.addSessionId(response, token);
    }

    //根据分布式session ID读取user
    public UserInf getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        UserInf userInf = redisUtil.get(UserKey.token, token, UserInf.class);
        //延长有效期，在每次最后访问时重新刷新session时限
        if(userInf != null) {
            addSession(response, token, userInf);
        }
        return userInf;
    }
}
