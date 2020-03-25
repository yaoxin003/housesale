package com.yx.house.house.controller;

import com.yx.house.house.model.User;
import com.yx.house.house.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/05/9:51
 */
@Controller
public class HouseController {

    @Autowired
    private UserService userService;

    @RequestMapping("hello")
    public String hello(ModelMap modelMap){
        List<User> users = userService.getUsers();
        System.out.println(users);
        modelMap.put("users",users);
        return "hello";
    }

}
