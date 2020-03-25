package com.yx.house.service.apigateway.service;

import com.yx.house.service.apigateway.dao.HouseDao;
import com.yx.house.service.apigateway.model.House;
import com.yx.house.service.apigateway.page.PageData;
import com.yx.house.service.apigateway.page.PageParams;
import com.yx.house.service.apigateway.vo.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/02/10:14
 */
@Service
public class HouseService {

    @Autowired
    private HouseDao houseDao;

    public PageData<House> queryHouse(House query, PageParams build) {
        ListResponse<House> result = houseDao.getHouses(query,build.getLimit(),build.getOffset());
        return PageData.buildPage(result.getList(),result.getCount(),
                build.getPageSize(), build.getPageNum());
    }

    public List<House> getHotHouse(Integer recomSize) {
        List<House> list = houseDao.getHotHouse(recomSize);
        return list;
    }

    public House queryOneHouse(Long id) {
        return houseDao.getOneHouse(id);
    }

    public void bindUser2House(Long houseId, Long userId) {
        houseDao.bindUser2House(houseId,userId);

    }

    public void unbindUser2House(Long houseId, Long userId) {
        houseDao.unbindUser2House(houseId,userId);
    }
}
