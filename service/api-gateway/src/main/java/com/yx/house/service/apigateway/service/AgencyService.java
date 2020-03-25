package com.yx.house.service.apigateway.service;

import com.yx.house.service.apigateway.dao.UserDao;
import com.yx.house.service.apigateway.model.Agency;
import com.yx.house.service.apigateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/26/17:21
 */
@Service
public class AgencyService {

    @Autowired
    private UserDao userDao;

    public List<Agency> getAllAgency() {
        return userDao.getAllAgency();
    }

    public User getAgentDetail(Long userId) {
        return userDao.getAgentById(userId);
    }
}
