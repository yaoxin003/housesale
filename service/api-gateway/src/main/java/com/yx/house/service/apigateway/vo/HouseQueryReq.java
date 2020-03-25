package com.yx.house.service.apigateway.vo;


import com.yx.house.service.apigateway.model.House;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/29/10:31
 */
public class HouseQueryReq {
    private House query;
    private Integer limit;
    private Integer offset;

    public House getQuery() {
        return query;
    }

    public void setQuery(House query) {
        this.query = query;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "HouseQueryReq{" +
                "query=" + query +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
