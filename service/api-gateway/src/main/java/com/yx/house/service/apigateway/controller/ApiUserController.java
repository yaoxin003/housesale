package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.common.RestResponse;
import com.yx.house.service.apigateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/18:06
 */
@RestController
public class ApiUserController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @RequestMapping("test/getUserName")
    public RestResponse<String> getUserName(Long id){
        LOGGER.info("Incoming Request，id=" + id);
        return RestResponse.success(userService.getUserName(id));
    }
}
