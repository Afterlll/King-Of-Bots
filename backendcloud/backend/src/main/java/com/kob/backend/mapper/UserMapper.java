package com.kob.backend.mapper;

import com.kob.backend.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13547
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-08-22 16:25:24
* @Entity com.kob.backend.bean.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




