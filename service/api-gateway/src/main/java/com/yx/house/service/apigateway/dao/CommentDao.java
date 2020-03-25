package com.yx.house.service.apigateway.dao;

import com.yx.house.service.apigateway.common.RestResponse;
import com.yx.house.service.apigateway.common.Rests;
import com.yx.house.service.apigateway.config.GenericRest;
import com.yx.house.service.apigateway.model.Blog;
import com.yx.house.service.apigateway.model.Comment;
import com.yx.house.service.apigateway.vo.BlogQueryReq;
import com.yx.house.service.apigateway.vo.CommentReq;
import com.yx.house.service.apigateway.vo.ListResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/05/14:41
 */
@Repository
public class CommentDao {

    @Autowired
    private GenericRest rest;

    @Value("${comment.service.name}")
    private String commentServiceName;

    public List<Comment> listComment(CommentReq commentReq) {
        return Rests.exec( ()->{
           String url = Rests.toUrl(commentServiceName,"/comment/list");
           return rest.post(url,commentReq,
                    new ParameterizedTypeReference<RestResponse<List<Comment>>>(){}
            ).getBody();
        }).getResult();
    }

    public Pair<List<Blog>,Long> getBlogs(Blog query, Integer limit, Integer offset) {
        ListResponse<Blog> listResponse = Rests.exec( () -> {
            BlogQueryReq blogQueryReq = new BlogQueryReq();
            blogQueryReq.setBlog(query);
            blogQueryReq.setLimit(limit);
            blogQueryReq.setOffset(offset);
            String url = Rests.toUrl(commentServiceName, "/blog/list");
            return
                    rest.post(url, blogQueryReq, new ParameterizedTypeReference<RestResponse<ListResponse<Blog>>>() {
                    }).getBody();
        }).getResult();
       return ImmutablePair.of(listResponse.getList(),listResponse.getCount());
    }

    public Blog getBlog(Long id){
        return Rests.exec( ()->{
            String url = Rests.toUrl(commentServiceName,"/blog/one?id=" + id);
            return rest.get(url, new ParameterizedTypeReference<RestResponse<Blog>>() {
            }).getBody();
        }).getResult();
    }

}
