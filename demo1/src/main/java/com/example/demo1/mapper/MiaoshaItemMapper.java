package com.example.demo1.mapper;

import com.example.demo1.domain.MiaoshaItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wangzg
* @description 针对表【miaosha_item(秒杀商品表)】的数据库操作Mapper
* @createDate 2021-12-26 20:28:24
* @Entity com.example.demo1.domain.MiaoshaItem
*/
@Mapper
public interface MiaoshaItemMapper {
    /**
     * 获取全部秒杀商品
     * @return
     */
    List<MiaoshaItem> findAll();

    /**
     * 根据商品id查询秒杀商品
     * @param itemId
     * @return
     */
    MiaoshaItem findByItemId(@Param("itemId") long itemId);

    /**
     *  更新秒杀商品库存
     *  更新秒杀商品库存
     * @param miaoshaItem
     * @return
     */
    int reduceStock(MiaoshaItem miaoshaItem);
}




