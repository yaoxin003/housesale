package com.yx.house.service.house.controller;

import com.yx.house.service.house.common.CommonConstants;
import com.yx.house.service.house.common.LimitOffset;
import com.yx.house.service.house.common.RestCode;
import com.yx.house.service.house.common.RestResponse;
import com.yx.house.service.house.model.*;
import com.yx.house.service.house.service.HouseService;
import com.yx.house.service.house.service.RecommendService;
import com.yx.house.service.house.vo.HouseQueryReq;
import com.yx.house.service.house.vo.HouseUserReq;
import com.yx.house.service.house.vo.HouseUserType;
import com.yx.house.service.house.vo.ListResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/28/8:41
 */
@RestController
@RequestMapping("house")
public class HouseController {

    Logger logger = LoggerFactory.getLogger(HouseController.class);

    @Autowired
    private HouseService houseService;
    @Autowired
    private RecommendService recommendService;

    //房产新增
    @RequestMapping("add")
    public RestResponse<House> doAdd(@RequestBody House house){
        logger.info("doAdd");
        house.setState(CommonConstants.HOUSE_STATE_UP);
        if(house.getUserId() == null){
            return RestResponse.error(RestCode.ILLEGAL_PARAMS);
        }
        houseService.addHouse(house,house.getUserId());
        return RestResponse.success();
    }

    @RequestMapping("bind")
    public RestResponse<Object> delsale(@RequestBody HouseUserReq req){
        Integer bindType = req.getBindType();
        HouseUserType houseUserType = Objects.equals(bindType, 1) ?
                HouseUserType.SALE : HouseUserType.BOOKMARK;
        if(req.isUnBind()){
            houseService.unbindUser2House(req.getHouseId(),req.getUserId(),houseUserType);
        }else{
            houseService.bindUser2House(req.getHouseId(),req.getUserId(),houseUserType);
        }
        return RestResponse.success();
    }

    @RequestMapping("list")
    public RestResponse<ListResponse<House>> houseList(@RequestBody HouseQueryReq req){
        Integer limit = req.getLimit();
        Integer offset = req.getOffset();
        House query = req.getQuery();
        logger.info("【req=】"+ req);
        Pair<List<House>,Long> pair = houseService.queryHouse(query, LimitOffset.build(limit,offset));
        return RestResponse.success(ListResponse.build(pair.getKey(),pair.getValue()));
    }

    @RequestMapping("detail")
    public RestResponse<House> houseDetail(long id){
        House house = houseService.queryOneHouse(id);
        recommendService.increaseHot(id);
        return RestResponse.success(house);
    }

    //向经纪人发送留言通知
    @RequestMapping("addUserMsg")
    public RestResponse<Object> addHouseMsg(@RequestBody HouseMsg houseMsg){
        houseService.addHouseMsg(houseMsg);

        return RestResponse.success();
    }

    //更新评分
    @ResponseBody
    @RequestMapping("rating")
    public RestResponse<Object> houseRate(Double rating,@RequestParam("id") Long houseId){
        houseService.updateRating(houseId,rating);
        return RestResponse.success();
    }

    @RequestMapping("hot")
    public RestResponse<List<House>> getHotHouse(Integer size){
        List<House> list = recommendService.getHotHouse(size);
        return RestResponse.success(list);
    }

    @RequestMapping("lastest")
    public RestResponse<List<House>> getLastest(){
        List<House> houses = recommendService.getLastest();
        return RestResponse.success(houses);
    }

}
