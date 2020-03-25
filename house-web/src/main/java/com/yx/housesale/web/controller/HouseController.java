package com.yx.housesale.web.controller;

import com.yx.housesale.biz.service.*;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.model.*;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import com.yx.housesale.common.result.ResultMsg;
import com.yx.housesale.common.utils.BeanHelper;
import com.yx.housesale.web.interceptor.UserContext;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/14/15:55
 */
@Controller
@Log4j
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private CityService cityService;

    /**
        * @description: 1.实现分页查询
         * 2.支持小区名查询，类型查询
         * 3.支持排序
         * 4.支持展示图片，价格，标题，地址等
        * @author:  YX
        * @date:    2020/01/14 16:00
        * @param: pageSize 页大小
        * @param: pageNum 第几页
        * @param: query
        * @param: modelMap
        * @return: java.lang.String
        * @throws: 
        */
    @RequestMapping("house/list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        log.debug("【pageSize=】" + pageSize + "，【pageNum=】" + pageNum);
        log.debug("【query=】" + query );
        PageData<House> ps = houseService.getHouseList(query, PageParam.build(pageSize,pageNum));
        modelMap.put("ps",ps);
        modelMap.put("vo",query);
        log.debug("【ps=】" + ps );
        log.debug("【query=】" + query );

        this.buildHotHouses(4,modelMap);
        return "house/listing";
    }

    private void buildHotHouses(int pageSize,ModelMap modelMap){
        PageData<House> hotHouses = recommendService.getHotHouseList(pageSize);
        modelMap.put("hotHouses",hotHouses);
    }

    /**
        * @description:
        * 查询房屋详细
        * 查询关联经纪人
        * @author:  YX
        * @date:    2020/01/17 13:11
        * @param: id
        * @param: modelMap
        * @return: java.lang.String
        * @throws:
        */
    @RequestMapping("house/detail")
    public String houseDetail(Integer houseId,ModelMap modelMap){
        log.debug("【houseId=】" + houseId);
        if(houseId != null){
            House house = houseService.getOneHouseById(houseId);
            HouseUser houseUser = this.getOneSaleHouseUser(houseId);
            if(houseUser != null){
                Integer userId = houseUser.getUserId();
                User agent = userService.getAgentById(userId);
                modelMap.put("agent",agent);
            }
            modelMap.put("house",house);
            recommendService.increase(houseId.longValue());
        }
        this.buildHotHouses(4,modelMap);
        return "house/detail";
    }


    @RequestMapping("house/leaveMsg")
    public String leaveMsg(HouseMsg houseMsg){
        log.debug("【houseMsg=】" + houseMsg);
        BeanHelper.onInsert(houseMsg);
        int count = houseService.addHouseMsg(houseMsg);
        if(count == 1){
            User agent = userService.getAgentById(houseMsg.getUserId());
            mailService.sendMail("来自用户" + houseMsg.getEmail() + "的留言",houseMsg.getMsg(),agent.getEmail());
        }
        return "redirect:/house/detail?houseId=" + houseMsg.getHouseId();
    }

    private HouseUser getOneSaleHouseUser(Integer houseId){
        HouseUser houseUser = null;
        List<HouseUser> saleHouseUserList = houseService.getSaleHouseUserList(houseId);
        if(!saleHouseUserList.isEmpty()){
            houseUser = saleHouseUserList.get(0);
        }
        return houseUser;
    }

    @RequestMapping("house/toAdd")
    public String toAdd(ModelMap modelMap){
        List<Community> communities = houseService.getAllCommunities();
        List<City> cities = cityService.getAllCities();
        modelMap.put("communities",communities);
        modelMap.put("cities",cities);
        return "house/adding";
    }

    /**
        * @description:
         * 1.获取用户
         * 2.设置房产状态：上架
         * 3.添加房产
        * @author:  YX
        * @date:    2020/01/22 12:19
        * @param: house
        * @param: modelMap
        * @return: java.lang.String
        */
    @RequestMapping("house/add")
    public String add(House house,ModelMap modelMap){
       log.debug("【house=】" + house);
        User user = UserContext.getUser();//1.获取用户
        log.debug("【user=】" + user);
        house.setState(HouseConstant.DB_HOUSE_HOUSE_STATE_UP);// 2.设置房产状态：上架
        if(house != null && user != null){
            houseService.addHouse(user,house);//3.添加房产
        }
        return "redirect:/house/ownList";
    }

    @RequestMapping("house/ownList")
    public String ownList(House query ,Integer pageSize,Integer pageNum,ModelMap modelMap){
        log.debug("【query=】" + query + "，【pageSize=】" + pageSize + ",【pageNum=】" + pageNum);
        query.setHouseUserType(HouseConstant.DB_HOUSE_HOUSE_USER_TYPE_SALE);
        PageData<House> ps = this.pageHouse(query,pageSize,pageNum);
        modelMap.put("ps",ps);
        modelMap.put("pageType",HouseConstant.PAGE_TYPE_OWN);
        return "house/ownListing";
    }

    //评分
    @RequestMapping("house/rating")
    @ResponseBody
    public ResultMsg rating(Long houseId,Double rating){
        log.debug("【houseId=】" + houseId + "，【rating=】" + rating);
        houseService.updateRatingById(houseId,rating);
        ResultMsg resultMsg = ResultMsg.successMsg("ok");
        return resultMsg;
    }

    //收藏
    @RequestMapping("house/bookmark")
    @ResponseBody
    public ResultMsg bookmark(Long houseId){
        User user = UserContext.getUser();
        houseService.bindUser2House(houseId,user.getId(),false);
        return ResultMsg.successMsg("ok");
    }


    //取消收藏
    @RequestMapping("house/unBookmark")
    @ResponseBody
    public ResultMsg unBookmark(Long houseId){
        log.debug("【houseId=】" + houseId);
        User user = UserContext.getUser();
        houseService.unbindUser2House(houseId,user.getId(), false);
        return ResultMsg.successMsg("ok");
    }

    //收藏列表页面
    @RequestMapping("house/bookmarkList")
    public String bookmarkList(House query,Integer pageSize,Integer pageNum,ModelMap modelMap){
        log.debug("【query=】" + query + "，【pageSize=】" + pageSize + ",【pageNum=】" + pageNum);
        query.setHouseUserType(HouseConstant.DB_HOUSE_HOUSE_USER_TYPE_COLLECTION);
        PageData<House> ps = this.pageHouse(query,pageSize,pageNum);
        modelMap.put("ps",ps);
        modelMap.put("pageType",HouseConstant.PAGE_TYPE_BOOKMARK);
        return "house/ownListing";
    }

    private PageData<House> pageHouse(House query,Integer pageSize,Integer pageNum){
        User user = UserContext.getUser();
        log.debug("【user=】" + user);
        query.setUserId(user.getId());
        query.setJoinHouseUser(true);
        PageData<House> ps = houseService.getHouseList(query, PageParam.build(pageSize, pageNum));
        return ps;
    }

    //删除单条房产（type=true售卖，type=false收藏）对应HouseUser.type字段
    @RequestMapping("house/delHouseUser")
    public String delHouseUser(Long houseId,Boolean type){
        log.debug("【houseId=】" + houseId + "，【type=】" + type);
        User user = UserContext.getUser();
        houseService.unbindUser2House(houseId,user.getId(), type);
        if(type){//type=true售卖，type=false收藏
            return "redirect:/house/ownList";
        }else{
           return "redirect:/house/bookmarkList";
        }
    }
}
