package com.yx.house.service.comment.vo;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/12:46
 */
public class CommentReq {
    private Long userId;
    private Long houseId;
    private Long blogId;
    private String content;
    private Integer type;//1-房产，2-博客百科
    private Integer size;//获取多少评论

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

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "CommentReq{" +
                "userId=" + userId +
                ", houseId=" + houseId +
                ", blogId=" + blogId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
