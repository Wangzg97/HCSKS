package com.example.demo1.common.vo;

import com.example.demo1.domain.MiaoshaItem;
import com.example.demo1.domain.UserInf;
import lombok.Data;

import java.util.Map;

@Data
public class ItemDetailVo {
    private int remainSeconds = 0;
    private int leftSeconds = 0;
    private Map<String, Object> miaoshaItem;
    private UserInf user;
}
