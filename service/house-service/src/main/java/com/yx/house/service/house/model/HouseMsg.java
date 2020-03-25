package com.yx.house.service.house.model;

import javax.persistence.Transient;
import java.util.Date;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/18/9:31
 */
public class HouseMsg {
    private Long id;

    private String msg;

    private Date createTime;

    private Date updateTime;

    private Long userId;

    private Long houseId;

    private String userName;

    @Transient
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "HouseMsg{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                ", houseId=" + houseId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
