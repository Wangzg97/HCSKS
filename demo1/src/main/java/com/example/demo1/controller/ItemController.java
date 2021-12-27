package com.example.demo1.controller;

import com.example.demo1.access.AccessLimit;
import com.example.demo1.common.Result;
import com.example.demo1.common.vo.ItemDetailVo;
import com.example.demo1.domain.MiaoshaItem;
import com.example.demo1.domain.UserInf;
import com.example.demo1.service.MiaoshaService;
import com.example.demo1.util.ItemKey;
import com.example.demo1.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
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

    @GetMapping("/detail/{itemId}")
    @ResponseBody
    @AccessLimit
    public Result<ItemDetailVo> detail(UserInf user, @PathVariable("itemId") long itemId) {
        Map<String, Object> miaoshaItem = miaoshaService.getMiaoshaItemById(itemId);
        //获取秒杀的开始时间
        long startAt = fromLdt((LocalDateTime)miaoshaItem.get("start_date")).getTime();
        //获取秒杀的结束时间
        long endAt = fromLdt((LocalDateTime)miaoshaItem.get("end_date")).getTime();
        long now = System.currentTimeMillis();
        //距离秒杀开始的时间差值
        int remainSeconds;
        if (now < startAt) {
            // 秒杀未开始
            remainSeconds = (int)((startAt - now)/1000);
        }else if (now > endAt) {
            //秒杀已结束
            remainSeconds = -1;
        }else {
            //秒杀进行中
            remainSeconds = 0;
        }
        //距离秒杀结束的时间差值
        int leftSeconds = (int) ((endAt - now)/1000);
        //封装秒杀详情
        ItemDetailVo itemDetailVo = new ItemDetailVo();
        itemDetailVo.setRemainSeconds(remainSeconds);
        itemDetailVo.setLeftSeconds(leftSeconds);
        itemDetailVo.setMiaoshaItem(miaoshaItem);
        itemDetailVo.setUser(user);

        return Result.success(itemDetailVo);
    }

    static public LocalDateTime toLdt(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        ZonedDateTime zdt = cal.toZonedDateTime();
        return zdt.toLocalDateTime();
    }

    static public Date fromLdt(LocalDateTime ldt) {
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        GregorianCalendar cal = GregorianCalendar.from(zdt);
        return cal.getTime();
    }
}
