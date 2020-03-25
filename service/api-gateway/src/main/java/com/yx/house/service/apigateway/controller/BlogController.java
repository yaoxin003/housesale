package com.yx.house.service.apigateway.controller;

import com.yx.house.service.apigateway.common.CommonConstants;
import com.yx.house.service.apigateway.model.Blog;
import com.yx.house.service.apigateway.model.Comment;
import com.yx.house.service.apigateway.model.House;
import com.yx.house.service.apigateway.page.PageData;
import com.yx.house.service.apigateway.page.PageParams;
import com.yx.house.service.apigateway.service.CommentService;
import com.yx.house.service.apigateway.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/06/12:13
 */
@Controller
public class BlogController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private HouseService houseService;

    @RequestMapping("blog/list")
    public String list(Blog query, Integer pageSize, Integer pageNum, ModelMap modelMap){
        PageData<Blog> ps = commentService.queryBlogs(query, PageParams.build(pageSize,pageNum));
        List<House> houses = houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("ps",ps);
        modelMap.put("recomHouses",houses);
        return "/blog/listing";
    }

    @RequestMapping("blog/detail")
    public String blogDetail(Long id,ModelMap modelMap){
        Blog blog = commentService.queryOneBlog(id);
        List<Comment> comments = commentService.getBlogComments(id);
        List<House> houses = houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",houses);
        modelMap.put("blog",blog);
        modelMap.put("commentList",comments);
        return "/blog/detail";
    }
}
