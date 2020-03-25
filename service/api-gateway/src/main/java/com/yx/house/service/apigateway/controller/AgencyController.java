package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.model.User;
import com.yx.house.service.apigateway.service.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/27/12:40
 */
@Controller
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @RequestMapping("agency/detail")
    public String agentDetail(Long id,ModelMap modelMap){
        User user = agencyService.getAgentDetail(id);
        modelMap.put("agent",user);
        return "/user/agent/agentDetail";
    }
}
