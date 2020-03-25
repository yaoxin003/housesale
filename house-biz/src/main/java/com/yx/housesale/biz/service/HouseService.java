package com.yx.housesale.biz.service;

import com.yx.housesale.common.constant.HouseUserType;
import com.yx.housesale.common.model.*;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/14/18:33
 */
public interface HouseService {

    public PageData<House> getHouseList(House query, PageParam build);

    public House getOneHouseById(Integer id);

    public List<HouseUser> getSaleHouseUserList(Integer houseId);

    public Integer addHouseMsg(HouseMsg houseMsg);

    public List<Community> getAllCommunities();

    public void bindUser2House(Long houseId, Long userId, boolean flag);

    public int unbindUser2House(Long houseId, Long userId, boolean flag);

    public void addHouse(User user, House house);

    public int updateRatingById(Long houseId,Double rating);
}
