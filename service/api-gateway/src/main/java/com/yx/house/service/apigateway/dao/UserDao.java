package com.yx.house.service.apigateway.dao;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yx.house.service.apigateway.common.RestResponse;
import com.yx.house.service.apigateway.common.Rests;
import com.yx.house.service.apigateway.config.GenericRest;
import com.yx.house.service.apigateway.model.Agency;
import com.yx.house.service.apigateway.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/15:59
 */
@Repository
@DefaultProperties(groupKey = "userDao",threadPoolKey = "userDao",
commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1000")},
threadPoolProperties={@HystrixProperty(name="coreSize",value="10"),
@HystrixProperty(name="maxQueueSize",value="1000")}
)
public class UserDao {

    Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private GenericRest rest;

    @Value("${user.service.name}")
    private String userServiceName;

    @HystrixCommand
    public User addUser(User account){
        String url = "http://" + userServiceName + "/user/add";
        logger.info("【url=】" + url);
        RestResponse<User> response = rest.post(url, account,
                new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if (response.getCode() == 0) {
            return response.getResult();
        }else{
            throw new IllegalStateException("Can not add user");
        }
    }

    @HystrixCommand
    public List<User> getUserList(User paramUser) {
        String url = "http://" + userServiceName + "/user/getList";
        logger.info("【url=】" + url);
        RestResponse<List<User>> restResponse = rest.post(url, paramUser,
                new ParameterizedTypeReference<RestResponse<List<User>>>() {
                }).getBody();
        if(restResponse.getCode() == 0){
            return restResponse.getResult();
        }else{
            return new ArrayList<>();
        }
    }

    @HystrixCommand
    public String getUserName(Long id){
        String url = "http://" + userServiceName + "/getUserName?id=" +id;
        RestResponse<String> response = rest.get(url,
                new ParameterizedTypeReference<RestResponse<String>>(){}).getBody();
        return response.getResult();
    }

    @HystrixCommand
    public User authUser(User user) {
        User retUser = null;
        String url = "http://" + userServiceName + "/user/auth";
        logger.debug("【url=】" + url);
        RestResponse<User> response = rest.post(url, user,
                new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if(response.getCode() == 0){
            retUser = response.getResult();
            return retUser;
        }
        logger.debug("【retUser=】" + retUser);
        throw new IllegalStateException("Can not auth user");
    }

    @HystrixCommand
    public void logout(String token) {
        String url = "http://" + userServiceName + "/user/logout?token=" + token;
        rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});
    }

    public User getUserByTokenFb(String token){
        return new User();
    }

    @HystrixCommand(fallbackMethod = "getUserByTokenFb")
    public User getUserByToken(String token) {
        logger.info("【token=】" + token);
        String url = "http://" + userServiceName + "/user/get?token=" + token;
        RestResponse<User> resp = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
        }).getBody();
        if(resp == null || resp.getCode() != 0){
            return null;
        }
        return resp.getResult();
    }

    @HystrixCommand
    public List<Agency> getAllAgency() {
        return Rests.exec(()->{
           String url = Rests.toUrl(userServiceName,"/agency/list");
            ResponseEntity<RestResponse<List<Agency>>> responseEntity =
                rest.get(url, new ParameterizedTypeReference<RestResponse<List<Agency>>>() {
            }) ;
            return responseEntity.getBody();
        }).getResult();
    }

    @HystrixCommand
    public User updateUser(User user) {
        return Rests.exec( () -> {
           String url = Rests.toUrl(userServiceName,"user/update");
            ResponseEntity<RestResponse<User>> responseEntity = rest.post(
                    url, user, new ParameterizedTypeReference<RestResponse<User>>() {
            });
            return responseEntity.getBody();
        }).getResult();
    }

    @HystrixCommand
    public User getAgentById(Long id) {
        return Rests.exec( () ->{
            String url = Rests.toUrl(userServiceName,"/agency/detail?id="+id);
            ResponseEntity<RestResponse<User>> responseEntity = rest.get(
                    url, new ParameterizedTypeReference<RestResponse<User>>() {
            });
            return responseEntity.getBody();
        }).getResult();
    }
}
