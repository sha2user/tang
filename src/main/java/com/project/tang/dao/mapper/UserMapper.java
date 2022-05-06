package com.project.tang.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.tang.dao.pojo.User;
import org.springframework.stereotype.Repository;

@Repository //添加该注解，消除idea中 userMapper等报错
public interface UserMapper extends BaseMapper<User> {
    String getAvatarByUsername(String username);
}
