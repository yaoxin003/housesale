package com.yx.house.service.apigateway.vo;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/02/10:29
 */
public class ListResponse<T> {
    private List<T> list;
    private Long count;

    public static <T> ListResponse<T> build(List<T> list, Long count){
        ListResponse<T> response = new ListResponse<>();
        response.setList(list);
        response.setCount(count);
        return response;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "list=" + list +
                ", count=" + count +
                '}';
    }
}
