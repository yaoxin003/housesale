package com.yx.house.service.apigateway.dao;

import com.yx.house.service.apigateway.common.RestResponse;
import com.yx.house.service.apigateway.common.Rests;
import com.yx.house.service.apigateway.config.GenericRest;
import com.yx.house.service.apigateway.constants.HouseUserType;
import com.yx.house.service.apigateway.model.House;
import com.yx.house.service.apigateway.vo.HouseQueryReq;
import com.yx.house.service.apigateway.vo.HouseUserReq;
import com.yx.house.service.apigateway.vo.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/02/10:23
 */
@Repository
public class HouseDao {
    @Autowired
    private GenericRest rest;

    @Value("${house.service.name}")
    private String houseServiceName;

    public ListResponse<House> getHouses(House query, Integer limit, Integer offset) {
        RestResponse<ListResponse<House>> resp = Rests.exec(() -> {
            HouseQueryReq req = new HouseQueryReq();
            req.setLimit(limit);
            req.setOffset(offset);
            req.setQuery(query);
            String url = Rests.toUrl(houseServiceName, "/house/list");
            ResponseEntity<RestResponse<ListResponse<House>>> responseEntity =
                    rest.post(url, req, new ParameterizedTypeReference<RestResponse<ListResponse<House>>>() {
                    });
            return responseEntity.getBody();
        });
        return resp.getResult();
    }

    public House getOneHouse(Long id) {
        return Rests.exec( ()->{
           String url = Rests.toUrl(houseServiceName,"/house/detail?id=" + id);
            return rest.get(url, new ParameterizedTypeReference<RestResponse<House>>() {
            }).getBody();
        }).getResult();
    }

    public List<House> getHotHouse(Integer recomSize) {
        return Rests.exec( () ->{
            String url = Rests.toUrl(houseServiceName,"/house/hot?size=" + recomSize);
            return rest.get(url, new ParameterizedTypeReference<RestResponse<List<House>>>() {
            }).getBody();
        }).getResult();
    }

    public void bindUser2House(Long houseId, Long userId) {
        HouseUserReq req = new HouseUserReq();
        req.setHouseId(houseId);
        req.setUserId(userId);
        req.setUnBind(false);
        req.setBindType(HouseUserType.BOOKMARK.value);
        this.bindOrInBind(req);
    }

    public void unbindUser2House(Long houseId, Long userId) {
        HouseUserReq req = new HouseUserReq();
        req.setHouseId(houseId);
        req.setUserId(userId);
        req.setUnBind(true);
        req.setBindType(HouseUserType.BOOKMARK.value);
        this.bindOrInBind(req);
    }

    private void bindOrInBind(HouseUserReq req) {
        Rests.exec( () -> {
            String url = Rests.toUrl(houseServiceName, "/house/bind");
            return rest.post(url, req, new ParameterizedTypeReference<RestResponse<Object>>() {
            }).getBody();
        });
    }
}
