package com.yx.housesale.biz.service.impl;

import com.yx.housesale.biz.service.CityService;
import com.yx.housesale.common.model.City;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/21/9:49
 */
@Service
public class CityServiceImpl implements CityService{

    @Override
    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        City city = new City();
        city.setId(1);
        city.setCityCode("110000");
        city.setCityName("北京市");
        cities.add(city);
        return cities;
    }
}
