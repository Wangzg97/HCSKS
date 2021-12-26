package com.example.demo1.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * 秒杀订单表
 * @TableName miaosha_order
 */
@Data
public class MiaoshaOrder implements Serializable {
    /**
     * 
     */
    private Long miaoshaOrderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long itemId;

    private static final long serialVersionUID = 1L;
}