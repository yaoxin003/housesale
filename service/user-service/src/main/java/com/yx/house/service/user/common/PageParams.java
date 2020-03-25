package com.yx.house.service.user.common;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/23/9:46
 */
public class PageParams {

    private static final Integer PAGE_SIZE = 2;
    private Integer pageSize;//每页多少条
    private Integer pageNum;//第几页
    private Integer offset;//查询语句使用开始查询条数
    private Integer limit;//查询语句每页多少条

    public static PageParams build(Integer pageSize,Integer pageNum){
        if(pageSize == null){
            pageSize = PAGE_SIZE;
        }
        if(pageNum == null){
            pageNum = 1;
        }
        return new PageParams(pageSize,pageNum);
    }

    public PageParams(){
        this(PAGE_SIZE,1);
    }

    public PageParams(int pageSize,int pageNum){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.offset = (pageNum-1)* pageSize;
        if(offset == 0){
            offset = null;
        }
        this.limit = pageSize;
    }

    public static Integer getPageSize() {
        return PAGE_SIZE;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PageParams{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
