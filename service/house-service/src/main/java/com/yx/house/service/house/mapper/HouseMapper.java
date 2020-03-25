package com.yx.house.service.house.mapper;

import com.yx.house.service.house.common.LimitOffset;
import com.yx.house.service.house.model.House;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/28/8:42
 */
public interface HouseMapper extends Mapper<House>{

    public List<House> selectHouse(@Param("house") House houseQuery, @Param("pageParams")
            LimitOffset pageParams);

    public Long selectHouseCount(@Param("house") House house);
}
