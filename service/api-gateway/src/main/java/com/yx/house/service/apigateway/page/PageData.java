package com.yx.house.service.apigateway.page;


import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/15/9:14
 */
public class PageData<T> {
    private List<T> list;
    private Pagination pagination;

    public PageData(Pagination pagination, List<T> list){
        this.pagination = pagination;
        this.list = list;
    }

    public static <T> PageData<T> buildPage(List<T> list, long count,
                                            Integer pageSize, Integer pageNum){
        Pagination pagination = new Pagination(pageSize,pageNum,count);
        return new PageData<>(pagination,list);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "list=" + list +
                ", pagination=" + pagination +
                '}';
    }
}
