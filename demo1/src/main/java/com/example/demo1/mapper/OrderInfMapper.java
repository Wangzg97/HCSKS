package com.example.demo1.mapper;

import com.example.demo1.domain.OrderInf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author wangzg
* @description 针对表【order_inf(订单表)】的数据库操作Mapper
* @createDate 2021-12-26 20:28:24
* @Entity com.example.demo1.domain.OrderInf
*/
@Mapper
public interface OrderInfMapper {
    /**
     * 插入新订单
     * @param orderInf
     * @return
     */
    long saveOrder(OrderInf orderInf);

    /**
     * 根据订单id和用户id查询订单
     * @param orderId
     * @param userId
     * @return
     */
    OrderInf findByIdAndUserId(@Param("orderId") long orderId, @Param("userId") long userId);
}




