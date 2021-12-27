package com.example.demo1.service;

import com.example.demo1.domain.MiaoshaItem;
import com.example.demo1.mapper.MiaoshaItemMapper;
import com.example.demo1.mapper.MiaoshaOrderMapper;
import com.example.demo1.mapper.OrderInfMapper;
import com.example.demo1.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MiaoshaService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MiaoshaItemMapper miaoshaItemMapper;
    @Autowired
    OrderInfMapper orderInfMapper;
    @Autowired
    MiaoshaOrderMapper miaoshaOrderMapper;

    public List<Map<String, Object>> listMiaoshaItem() {
        return miaoshaItemMapper.findAll();
    }
}
