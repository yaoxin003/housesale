package com.yx.house.service.apigateway.service;

import com.yx.house.service.apigateway.dao.UserDao;
import com.yx.house.service.apigateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/16:50
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String getUserName(Long id){
        return userDao.getUserName(id);
    }


}
