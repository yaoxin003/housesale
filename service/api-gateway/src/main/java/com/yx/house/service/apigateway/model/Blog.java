package com.yx.house.service.apigateway.model;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/15:45
 */
public class Blog {
    private Integer id;
    private String  tags;
    private String  content;
    private String  title;
    private Date createTime;
    private Date updateTIme;
    private Integer cat;//分类：1-准备买房，2-看房/选房，3-签约/定房，4-全款/贷款，5-缴税/过户，6-入住/交接，7-买房风险

    @Transient
    private String  digest;
    @Transient
    private List<String> tagList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTIme() {
        return updateTIme;
    }

    public void setUpdateTIme(Date updateTIme) {
        this.updateTIme = updateTIme;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", tags='" + tags + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", updateTIme=" + updateTIme +
                ", cat=" + cat +
                ", digest='" + digest + '\'' +
                ", tagList=" + tagList +
                '}';
    }
}
