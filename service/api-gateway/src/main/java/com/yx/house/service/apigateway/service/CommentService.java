package com.yx.house.service.apigateway.service;

import com.yx.house.service.apigateway.dao.CommentDao;
import com.yx.house.service.apigateway.model.Blog;
import com.yx.house.service.apigateway.model.Comment;
import com.yx.house.service.apigateway.page.PageData;
import com.yx.house.service.apigateway.page.PageParams;
import com.yx.house.service.apigateway.vo.CommentReq;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/02/11:39
 */
@Service
public class CommentService {


    @Autowired
    private CommentDao commentDao;

    public List<Comment> getHouseComments(Long houseId){
        CommentReq commentReq = new CommentReq();
        commentReq.setHouseId(houseId);
        commentReq.setType(1);
        commentReq.setSize(8);
        return commentDao.listComment(commentReq);
    }


    public List<Comment> getBlogComments(Long blogId) {
        CommentReq commentReq = new CommentReq();
        commentReq.setBlogId(blogId);
        commentReq.setType(2);
        commentReq.setSize(8);
        return commentDao.listComment(commentReq);
    }

    public PageData<Blog> queryBlogs(Blog query, PageParams build) {
        Pair<List<Blog>,Long> pair = commentDao.getBlogs(query,build.getLimit(),build.getOffset());
        return PageData.buildPage(pair.getKey(),pair.getValue(),build.getPageSize(),build.getPageNum());
    }

    public Blog queryOneBlog(Long id) {
        return commentDao.getBlog(id);
    }
}
