package com.yx.house.service.house.model;

import java.util.Date;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/17/15:37
 */
public class HouseUser {

    private Long id;

    private Long houseId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private Integer type;//1-售卖，2-收藏

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HouseUser{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                '}';
    }
}
