package com.yx.housesale.biz.service;

import com.yx.housesale.common.model.House;
import com.yx.housesale.common.page.PageData;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/20/12:58
 */

public interface RecommendService {


    public void increase(Long houseId);

    /**
     * @description: 获得热门房产集合
     * @author:  YX
     * @date:    2020/01/20 13:11
     * @param:
     * @return: java.util.List<java.lang.Integer>
     * @throws:
     */
    public PageData<House> getHotHouseList(Integer pageSize);

    public PageData<House> getLastestHouses();
}
