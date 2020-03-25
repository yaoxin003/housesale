package com.yx.house.service.house.service;

import com.yx.house.service.house.common.LimitOffset;
import com.yx.house.service.house.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/01/10:00
 */
@Service
public class RecommendService {

    private String HOT_HOUSE_KEY = "hot_house";

    @Autowired
    private HouseService houseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void increaseHot(long id) {
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY,""+id,1.0d);
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY,0,-11);
    }


    public List<House> getHotHouse(Integer size) {
        if(size == null){
            size = 3;
        }
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, size - 1);
        List<Long> ids = idSet.stream().map( b -> Long.parseLong(b)).collect(Collectors.toList());
        House query = new House();
        query.setIds(ids);
        return houseService.queryAndSetImg(query, LimitOffset.build(size,0));
    }

    public List<House> getLastest() {
        House query = new House();
        query.setSort("create_time");
        return houseService.queryAndSetImg(query,LimitOffset.build(8,0));
    }


}
