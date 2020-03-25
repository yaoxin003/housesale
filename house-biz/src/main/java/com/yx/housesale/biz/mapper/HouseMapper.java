package com.yx.housesale.biz.mapper;

import com.yx.housesale.common.model.House;
import com.yx.housesale.common.page.PageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/15/10:39
 */
public interface HouseMapper extends Mapper<House> {
    public List<House> selectPageHouses(
            @Param("house") House house,@Param("pageParam") PageParam pageParam);

    public int selectPageCount(@Param("house") House house);
}
