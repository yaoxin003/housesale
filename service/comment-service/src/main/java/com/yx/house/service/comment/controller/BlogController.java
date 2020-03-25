package com.yx.house.service.comment.controller;

import com.yx.house.service.comment.common.LimitOffset;
import com.yx.house.service.comment.common.RestResponse;
import com.yx.house.service.comment.model.Blog;
import com.yx.house.service.comment.service.BlogService;
import com.yx.house.service.comment.vo.BlogQueryReq;
import com.yx.house.service.comment.vo.ListResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/06/9:04
 */
@RestController
@RequestMapping("blog")
public class BlogController {

    Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @RequestMapping("list")
    public RestResponse<ListResponse<Blog>> list(@RequestBody BlogQueryReq req){
        logger.debug("【BlogQueryReq=】" + req);
        Integer limit = req.getLimit();
        Integer offset = req.getOffset();
        Blog blog = req.getBlog();
        Pair<List<Blog>,Long> pair =
                blogService.queryBlog(blog,LimitOffset.build(limit,offset));
        return RestResponse.success(ListResponse.build(pair.getKey(),pair.getValue()));
    }

    @RequestMapping("one")
    public RestResponse<Blog> one(Integer id){
        Blog blog = blogService.queryOneBlog(id);
        return RestResponse.success(blog);
    }
}
