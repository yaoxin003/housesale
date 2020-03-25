package com.yx.house.service.comment.dao;

import com.yx.house.service.comment.common.RestResponse;
import com.yx.house.service.comment.common.Rests;
import com.yx.house.service.comment.config.GenericRest;
import com.yx.house.service.comment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/14:13
 */
@Repository
public class UserDao {
    @Autowired
    private GenericRest rest;

    @Value("${user.service.name}")
    private String userServiceName;

    public User getUserDetail(Long userId){
        return Rests.exec( () ->{
            String url = Rests.toUrl(userServiceName, "/user/getById?id=" + userId);
            return rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
            }).getBody();
        }).getResult();
    }
}
