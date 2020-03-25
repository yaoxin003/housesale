package com.yx.house.service.user.controller;

import com.yx.house.service.user.common.RestResponse;
import com.yx.house.service.user.exception.IllegalParamsException;
import com.yx.house.service.user.model.User;
import com.yx.house.service.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yx.house.service.user.exception.IllegalParamsException.Type;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/18:03
 */
@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private UserService userService;

    //查询
    @RequestMapping("getById")
    public RestResponse<User> getUserById(Long id){
        User user = userService.getUserById(id);
        return RestResponse.success(user);
    }

    @RequestMapping("getList")
    public RestResponse<List<User>> getUserList(@RequestBody User user){
        List<User> userList = userService.getUsersByQuery(user);
        return RestResponse.success(userList);
    }

    @RequestMapping("add")
    public RestResponse<User> add(@RequestBody User user){
        userService.addAccount(user,user.getEnableUrl());
        return RestResponse.success();
    }

    @RequestMapping("enable")
    public RestResponse<User> enable(String key){
        userService.enable(key);
        return RestResponse.success();
    }

    //鉴权
    @RequestMapping("auth")
    public RestResponse<User> auth(@RequestBody User user){
        LOGGER.debug("auth begin");
        User findUser = userService.auth(user.getEmail(),user.getPasswd());
        return RestResponse.success(findUser);
    }

    //登录
   @RequestMapping("get")
    public RestResponse<User> getUser(String token){
       LOGGER.debug("begin sleep");
        User user = userService.getLoginedUserByToken(token);
        return RestResponse.success(user);
    }

    @RequestMapping("logout")
    public RestResponse<User> logout(String token){
        userService.invalidate(token);
        return RestResponse.success();
    }

    @RequestMapping("update")
    public RestResponse<User> update(User user){
        User updateUser = userService.updateUser(user);
        return RestResponse.success(updateUser);
    }

    /**
     * 练习
     * @param id
     * @return
     */
    @RequestMapping("testGetUserName")
    public RestResponse<String> testGetUserName(Long id){
        LOGGER.info("id=" + id);
        if( id == null || id==0){
            throw new IllegalParamsException(Type.WRONG_PAGE_NUM,"错误分页");
        }
        LOGGER.info("Incoming Request server port：" + port);
        return RestResponse.success("test-username " + port);
    }

}
