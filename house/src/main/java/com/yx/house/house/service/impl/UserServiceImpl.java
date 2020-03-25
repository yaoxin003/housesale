package com.yx.house.house.service.impl;

import com.yx.house.house.mapper.UserMapper;
import com.yx.house.house.model.User;
import com.yx.house.house.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/05/10:04
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers(){
            return userMapper.selectAll();
    }
}
