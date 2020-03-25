package com.yx.house.service.apigateway.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 分页底部工具栏
 * @author: yx
 * @date: 2020/01/14/18:49
 */
public class Pagination {
    private int pageSize;
    private int pageNum;
    private long totalCount;
    private Long pageCount;
    private List<Integer> pages = new ArrayList<>();

    public Pagination(Integer pageSize, Integer pageNum, Long totalCount){
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalCount = totalCount;
        for(int i=1; i<=pageNum; i++){
            pages.add(i);
        }
        Long pageCount = totalCount/pageSize + ((totalCount%pageSize == 0) ? 0 : 1);
        if(pageCount > pageNum){
            for(int i=pageNum+1; i<=pageCount; i++){
                pages.add(i);
            }
        }
        this.pageCount =pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", totalCount=" + totalCount +
                ", pageCount=" + pageCount +
                ", pages=" + pages +
                '}';
    }
}
