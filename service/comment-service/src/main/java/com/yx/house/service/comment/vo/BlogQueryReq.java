package com.yx.house.service.comment.vo;

import com.yx.house.service.comment.model.Blog;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/06/9:14
 */
public class BlogQueryReq {

    private Blog blog;

    private Integer limit;

    private Integer offset;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
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
        return "BlogQueryReq{" +
                "blog=" + blog +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
