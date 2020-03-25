package com.yx.house.service.comment.controller;

import com.yx.house.service.comment.common.RestResponse;
import com.yx.house.service.comment.model.Comment;
import com.yx.house.service.comment.service.CommentService;
import com.yx.house.service.comment.vo.CommentReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/12:24
 */
@RestController
@RequestMapping("comment")
public class CommentController {

    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @RequestMapping("list")
    public RestResponse<List<Comment>> list(@RequestBody CommentReq commentReq){
        Integer type = commentReq.getType();
        List<Comment> list = null;
        if(Objects.equals(1,type)){
            list = commentService.getHouseComments(commentReq.getHouseId(),commentReq.getSize());
        }else{
            list = commentService.getBlogComments(commentReq.getBlogId(),commentReq.getSize());
        }
        logger.debug("【list=】" + list);
        return RestResponse.success(list);
    }
}
