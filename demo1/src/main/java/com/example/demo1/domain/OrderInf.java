package com.example.demo1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @TableName order_inf
 */
@Data
public class OrderInf implements Serializable {
    /**
     * 
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 冗余的品名，用户避免多表连接
     */
    private String itemName;

    /**
     * 购买的商品数量
     */
    private Integer orderNum;

    /**
     * 购买价格
     */
    private BigDecimal orderPrice;

    /**
     * 渠道：1-PC；2-Android，3-IOS
     */
    private Integer orderChannel;

    /**
     * 订单状态：0-新建未支付，1-已支付，2-已发货，3-已收货，4-已退款，5-已完成
     */
    private Integer orderStatus;

    /**
     * 订单创建时间
     */
    private Date createDate;

    /**
     * 支付时间
     */
    private Date payDate;

    private static final long serialVersionUID = 1L;
}