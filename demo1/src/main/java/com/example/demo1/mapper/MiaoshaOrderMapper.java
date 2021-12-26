package com.example.demo1.mapper;

import com.example.demo1.domain.MiaoshaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author wangzg
* @description 针对表【miaosha_order(秒杀订单表)】的数据库操作Mapper
* @createDate 2021-12-26 20:28:24
* @Entity com.example.demo1.domain.MiaoshaOrder
*/
@Mapper
public interface MiaoshaOrderMapper {
    /**
     * 根据用户id和商品id获取秒杀订单
     * @param userId
     * @param itemId
     * @return
     */
    MiaoshaOrder findByUserIdAndItemId(@Param("userId") long userId, @Param("itemId") long itemId);

    /**
     * 保存秒杀订单
     * @param miaoshaOrder
     * @return
     */
    int save(MiaoshaOrder miaoshaOrder);
}




