package com.yx.housesale.common.constant;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/12/10:08
 */
public class HouseConstant {
    /**---------------------------数据库字段 关系数据库 库名 表名 字段名 常量---------------------------**/
    //user表
    public static final Integer DB_HOUSE_USER_ENABLE_STOP = 0;//停用
    public static final Integer DB_HOUSE_USER_ENABLE_START = 1;//启用
    public static final Integer DB_HOUSE_USER_TYPE_COMMON_USER = 1;//1.普通用户
    public static final Integer DB_HOUSE_USER_TYPE_AGENT = 2;//2.经纪人

    //house表
    public static final Integer DB_HOUSE_HOUSE_TYPE_SALE = 1;//销售
    public static final Integer DB_HOUSE_HOUSE_TYPE_RENT = 1;//出租
    public static final Integer DB_HOUSE_HOUSE_STATE_UP = 1;//上架
    public static final Integer DB_HOUSE_HOUSE_STATE_DOWN = 1;//下架

    //house_user表
    public static final Integer DB_HOUSE_HOUSE_USER_TYPE_SALE = 1;//1-售卖 枚举类HouseUserType.value
    public static final Integer DB_HOUSE_HOUSE_USER_TYPE_COLLECTION = 2;//2-收藏 枚举类HouseUserType.value

    /**---------------------------session---------------------------**/
    public static final String SESSION_USER_ATTRIBUTE = "loginUser";

    /**---------------------------cache---------------------------**/
    public static final String CACHE_HOT_HOUSES_KEY = "HOT_HOUSES_KEY";//热门房产

    /**---------------------------页面---------------------------**/
    public static final String PAGE_TYPE_OWN = "own";//自己
    public static final String PAGE_TYPE_BOOKMARK = "bookmark";//收藏




}
