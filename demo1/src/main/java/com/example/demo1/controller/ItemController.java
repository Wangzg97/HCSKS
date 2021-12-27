package com.example.demo1.controller;

import com.example.demo1.access.AccessLimit;
import com.example.demo1.domain.MiaoshaItem;
import com.example.demo1.domain.UserInf;
import com.example.demo1.service.MiaoshaService;
import com.example.demo1.util.ItemKey;
import com.example.demo1.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @GetMapping("/list")
    @ResponseBody
    @AccessLimit //限制必须登陆后才能使用
    public String list(HttpServletRequest request, HttpServletResponse response, UserInf userInf) {
        String html = redisUtil.get(ItemKey.itemList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //查询秒杀商品列表存入redis缓存
        List<Map<String, Object>> itemList = miaoshaService.listMiaoshaItem();
        IWebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), Map.of("user", userInf, "itemList", itemList));
        //渲染静态的html页面
        html = thymeleafViewResolver.getTemplateEngine().process("item_list", ctx);
        //将静态的html存入redis
        if (!StringUtils.isEmpty(html)) {
            redisUtil.set(ItemKey.itemList, "", html);
        }

        return html;
    }
}
