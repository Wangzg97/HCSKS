package com.example.demo1.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀商品表
 * @TableName miaosha_item
 */
@Data
public class MiaoshaItem implements Serializable {
    /**
     * 秒杀的商品表
     */
    private Long miaoshaId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 秒杀价
     */
    private BigDecimal miaoshaPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;

    private static final long serialVersionUID = 1L;
}