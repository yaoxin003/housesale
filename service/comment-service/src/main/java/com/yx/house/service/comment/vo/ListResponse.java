package com.yx.house.service.comment.vo;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/06/9:10
 */
public class ListResponse<T> {

    private Long count;

    private List<T> list;

    public static <T> ListResponse<T> build(List<T> list,Long count){
        ListResponse<T> response = new ListResponse<>();
        response.setList(list);
        response.setCount(count);
        return response;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "count=" + count +
                ", list=" + list +
                '}';
    }
}
