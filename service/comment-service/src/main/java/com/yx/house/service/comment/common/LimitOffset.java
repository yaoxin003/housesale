package com.yx.house.service.comment.common;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/29/10:37
 */
public class LimitOffset {
    private Integer limit;
    private Integer offset;

    public static LimitOffset build(Integer limit,Integer offset){
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setLimit(limit);
        limitOffset.setOffset(offset);
        return limitOffset;
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
        return "LimitOffset{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
