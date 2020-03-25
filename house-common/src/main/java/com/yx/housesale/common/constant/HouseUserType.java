package com.yx.housesale.common.constant;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/22/14:26
 */
public enum  HouseUserType {

    SALE(1),//销售
    BOOKMARK(2);//书签

    public final Integer value;

    private HouseUserType(Integer value){
        this.value = value;
    }
}
