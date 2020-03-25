package com.yx.housesale.biz.service.impl;

import com.yx.housesale.biz.service.HouseService;
import com.yx.housesale.biz.service.RecommendService;
import com.yx.housesale.biz.util.RedisUtil;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.model.House;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/20/12:59
 */
@Service
@Log4j
public class RecommendServiceImpl implements RecommendService {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HouseService houseService;

    public void increase(Long houseId){
        Jedis jedis = redisUtil.getJedis();
        Double zincrby = jedis.zincrby(HouseConstant.CACHE_HOT_HOUSES_KEY, 1.0d, String.valueOf(houseId));
        log.debug("【zincrby=】" + zincrby);
        jedis.close();
    }

    /**
        * @description: 获得热门房产Id集合
        * @author:  YX
        * @date:    2020/01/20 13:11
        * @param:
        * @return: java.util.List<java.lang.Integer>
        * @throws:
        */
    private List<Long> getHotHouseIds(){
        Jedis jedis = redisUtil.getJedis();
        List<Long> retIds = null;
        if(jedis != null){
            jedis = redisUtil.getJedis();
            Set<String> idSet =jedis.zrange(HouseConstant.CACHE_HOT_HOUSES_KEY, 0, -1);
            retIds = idSet.stream().map(Long::valueOf).collect(Collectors.toList());
            jedis.close();
        }
        log.debug("【retIds=】" + retIds);
        return retIds;
    }

    /**
     * @description: 获得热门房产集合
     * @author:  YX
     * @date:    2020/01/20 13:11
     * @param:
     * @return: java.util.List<java.lang.Integer>
     * @throws:
     */
    public PageData<House> getHotHouseList(Integer pageSize){
        House query = new House();
        List<Long> retIds = this.getHotHouseIds();
        PageData<House> houseList = null;
        if(retIds != null){
            query.setIds(retIds);
            houseList = houseService.getHouseList(query, PageParam.build(pageSize, 1));
        }
        return houseList;
    }

    @Override
    public PageData<House> getLastestHouses() {
        House query = new House();
        query.setSort("create_time desc");
        PageData<House> houseList = houseService.getHouseList(query, PageParam.build(8, 1));
        return houseList;
    }


}
