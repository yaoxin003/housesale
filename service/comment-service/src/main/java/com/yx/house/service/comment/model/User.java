package com.yx.house.service.comment.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/21/11:07
 */
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String passwd;
    private Integer type;//用户类型：1.普通用户，2.经纪人
    private String avatar;//头像图片
    @JSONField(deserialize = false,serialize = false)
    private MultipartFile avatorFile;//头像图片文件
    private String aboutme;//自我介绍
    private Integer enable;//是否启用：0停用，1.启用
    private Date createTime;//启动时间
    private Date updateTime;//修改时间
    private Integer agencyId;//所属经纪机构

    @Transient
    private String confirmPasswd;
    @Transient
    private String currPasswd;
    @Transient
    private List<Long> ids;
    @Transient
    private String enableUrl;
    @Transient
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public MultipartFile getAvatorFile() {
        return avatorFile;
    }

    public void setAvatorFile(MultipartFile avatorFile) {
        this.avatorFile = avatorFile;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public String getConfirmPasswd() {
        return confirmPasswd;
    }

    public void setConfirmPasswd(String confirmPasswd) {
        this.confirmPasswd = confirmPasswd;
    }

    public String getCurrPasswd() {
        return currPasswd;
    }

    public void setCurrPasswd(String currPasswd) {
        this.currPasswd = currPasswd;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getEnableUrl() {
        return enableUrl;
    }

    public void setEnableUrl(String enableUrl) {
        this.enableUrl = enableUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", passwd='" + passwd + '\'' +
                ", type=" + type +
                ", avatar='" + avatar + '\'' +
                ", avatorFile=" + avatorFile +
                ", aboutme='" + aboutme + '\'' +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", agencyId=" + agencyId +
                ", confirmPasswd='" + confirmPasswd + '\'' +
                ", currPasswd='" + currPasswd + '\'' +
                ", ids=" + ids +
                ", enableUrl='" + enableUrl + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
