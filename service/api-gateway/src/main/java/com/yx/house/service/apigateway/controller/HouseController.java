package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.common.CommonConstants;
import com.yx.house.service.apigateway.common.ResultMsg;
import com.yx.house.service.apigateway.common.UserContext;
import com.yx.house.service.apigateway.model.Comment;
import com.yx.house.service.apigateway.model.House;
import com.yx.house.service.apigateway.model.User;
import com.yx.house.service.apigateway.page.PageData;
import com.yx.house.service.apigateway.page.PageParams;
import com.yx.house.service.apigateway.service.AgencyService;
import com.yx.house.service.apigateway.service.CommentService;
import com.yx.house.service.apigateway.service.HouseService;
import com.yx.house.service.apigateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
public class HouseController {

    Logger logger = LoggerFactory.getLogger(HouseController.class);

    @Autowired
    private HouseService houseService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("house/list")
    public String list(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps = houseService.queryHouse(query, PageParams.build(pageSize,pageNum));
        List<House> rcHouses = houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("vo",query);
        modelMap.put("ps",ps);
        modelMap.put("recomHouses",rcHouses);
        return "house/listing";
    }

    @RequestMapping("house/detail")
    public String houseDetail(Long id, ModelMap modelMap){
        House house = houseService.queryOneHouse(id);
        List<House> rcHouses = houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        List<Comment> comments = commentService.getHouseComments(id);
        logger.debug("【comments=】" + comments);
        logger.debug("【rcHouses=】" + rcHouses);
        if(house.getUserId() != null){
            if(!Objects.equals(0,house.getUserId())){
                modelMap.put("agent",agencyService.getAgentDetail(house.getUserId()));
            }
        }
        modelMap.put("house",house);
        modelMap.put("recomHouses",rcHouses);
        modelMap.put("commentList",comments);
        return "house/detail";
    }

    @RequestMapping("house/bookmark")
    @ResponseBody
    public ResultMsg bookmark(@RequestParam("id") Long houseId, ModelMap modelMap, HttpServletRequest request){
        logger.info("【houseId=】" + houseId);
        User user = UserContext.getUser();
        logger.info("【userContext.user=】" + user);
        houseService.bindUser2House(houseId,user.getId());
        return ResultMsg.success();
    }

    @RequestMapping("house/unbookmark")
    @ResponseBody
    public ResultMsg unbookmark(@RequestParam("id") Long houseId, ModelMap modelMap){
        User user = UserContext.getUser();
        houseService.unbindUser2House(houseId,user.getId());
        return ResultMsg.success();
    }

}
