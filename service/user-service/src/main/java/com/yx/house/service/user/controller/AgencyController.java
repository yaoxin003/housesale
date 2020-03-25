package com.yx.house.service.user.controller;

import com.yx.house.service.user.common.PageParams;
import com.yx.house.service.user.common.RestResponse;
import com.yx.house.service.user.model.ListResponse;
import com.yx.house.service.user.model.User;
import com.yx.house.service.user.service.AgencyService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/23/12:48
 */
@RestController
@RequestMapping("agency")
public class AgencyController {

    Logger logger = LoggerFactory.getLogger(AgencyController.class);

    @Autowired
    private AgencyService agencyService;

    //经纪人列表
    @RequestMapping("list")
    public RestResponse<ListResponse<User>> agentList(Integer limit, Integer offset){
        PageParams pageParams = new PageParams();
        pageParams.setLimit(limit);
        pageParams.setOffset(offset);
        Pair<List<User>,Long> pair = agencyService.getAllAgent(pageParams);
        ListResponse<User> response = ListResponse.build(pair.getKey(),pair.getValue());
        return RestResponse.success(response);
    }

    @RequestMapping("detail")
    public RestResponse<User> agentDetail(Long id){
        logger.info("id=" + id);
        User user = agencyService.getAgentDetail(id);
        return RestResponse.success(user);
    }


}
