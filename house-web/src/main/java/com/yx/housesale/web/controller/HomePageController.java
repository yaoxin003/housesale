package com.yx.housesale.web.controller;

import com.yx.housesale.biz.service.RecommendService;
import com.yx.housesale.common.model.House;
import com.yx.housesale.common.page.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @description:首页
 * @author: yx
 * @date: 2020/01/20/13:50
 */
@Controller
public class HomePageController {

    @Autowired
    private RecommendService recommendService;

    @RequestMapping("index")
    public String index(ModelMap modelMap){
        PageData<House> ps = recommendService.getLastestHouses();//最新房产
        modelMap.put("ps",ps);
        return "homepage/index";
    }

    /**
        * @description: 在浏览器只输入域名后的操作
        * @author:  YX
        * @date:    2020/01/20 13:51
        * @param:
        * @return: java.lang.String
        * @throws:
        */
    @RequestMapping("")
    public String home(){
        return "redirect:/index";
    }


}
