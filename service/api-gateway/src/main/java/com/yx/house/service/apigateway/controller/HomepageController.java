package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.common.CommonConstants;
import com.yx.house.service.apigateway.model.House;
import com.yx.house.service.apigateway.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description:首页
 * @author: yx
 * @date: 2020/02/26/10:33
 */
@Controller
public class HomepageController {

    @Autowired
    private HouseService houseService;

    @RequestMapping("index")
    public String homePage(ModelMap modelMap){

        List<House> rcHouses = houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses", rcHouses);
        return "/homepage/index";
    }

    @RequestMapping("")
    public String index(){
        return "redirect:/index";
    }
}
