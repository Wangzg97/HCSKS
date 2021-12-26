package com.example.demo1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 商品表
 * @TableName item_inf
 */
@Data
public class ItemInf implements Serializable {
    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品的图片
     */
    private String itemImg;

    /**
     * 商品的详情介绍
     */
    private String itemDetail;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 商品库存，-1表示没有限制
     */
    private Integer stockNum;

    private static final long serialVersionUID = 1L;
}