package com.yx.house.service.house.dao;

import com.yx.house.service.house.common.RestResponse;
import com.yx.house.service.house.common.Rests;
import com.yx.house.service.house.config.GenericRest;
import com.yx.house.service.house.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/29/17:15
 */
@Repository
public class UserDao {

    @Autowired
    private GenericRest rest;

    @Value("${user.service.name}")
    private String userServiceName;

    public User getAgentDetail(Long agentId){
        RestResponse<User> response = Rests.exec( ()->{
            String url = Rests.toUrl(userServiceName,"/agency/detail?id=" + agentId);
            ResponseEntity<RestResponse<User>> responseEntity =
                    rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
            });
            return responseEntity.getBody();
        });
        return response.getResult();
    }
}
