package com.example.demo1.mapper;

import com.example.demo1.domain.UserInf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author wangzg
* @description 针对表【user_inf(秒杀用户表)】的数据库操作Mapper
* @createDate 2021-12-26 20:28:24
* @Entity com.example.demo1.domain.UserInf
*/
@Mapper
public interface UserInfMapper {

    UserInf findByUserId(@Param("id") long id);

    void updateUserInf(UserInf userInf);
}




