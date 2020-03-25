package com.yx.house.service.comment.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.yx.house.service.comment.dao.UserDao;
import com.yx.house.service.comment.mapper.CommentMapper;
import com.yx.house.service.comment.model.Comment;
import com.yx.house.service.comment.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/14:09
 */
@Service
public class CommentService {

    Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<Comment> getBlogComments(Long blogId, Integer size) {
        List<Comment> lists = null;
        String redisKey = "blog_comments_blogId:" + blogId + ":" +size;
        //查询缓存
        String json = redisTemplate.opsForValue().get(redisKey);
        if(Strings.isNullOrEmpty(json)){//缓存中无数据
            //查询数据库
            lists = this.doGetBlogComments(blogId,size);
            //插入缓存并设置失效时间
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(lists));
            redisTemplate.expire(redisKey,5, TimeUnit.SECONDS);
        }else{//缓存中有数据
            lists = JSON.parseObject(json,
                    new TypeReference<List<Comment>>(){});
        }
        return lists;
    }

    /**
        * @description:
        * 1.查询数据库获取评论列表
        * 2.调用用户服务获得头像
        * @author:  YX
        * @date:    2020/03/04 16:11
        * @param: blogId
        * @param: size
        * @return: java.util.List<com.yx.house.service.comment.model.Comment>
        * @throws:
        */
    private List<Comment> doGetBlogComments(Long blogId, Integer size) {
        logger.info("commentMapper=" + commentMapper);
        List<Comment> comments = commentMapper.selectBlogComments(blogId, size);
        comments.forEach( comment ->{
            User user = userDao.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }

/**
    * @description:
    * 1.查询缓存
    * 2.1.缓存中不存在，则查询数据库并存入缓存设置失效时间
    * 2.2.缓存存在返回集合
    * @author:  YX
    * @date:    2020/03/04 18:26
    * @param: houseId
    * @param: size
    * @return: java.util.List<com.yx.house.service.comment.model.Comment>
    * @throws:
    */
    public List<Comment> getHouseComments(Long houseId, Integer size) {
        String redisKey = "house_comments_houseId:" + houseId + ":" +size;
        //查询缓存
        String json = redisTemplate.opsForValue().get(redisKey);
        List<Comment> lists = null;
        if(Strings.isNullOrEmpty(json)){//缓存中无数据
            //查询数据库
            lists = this.doGetHouseComments(houseId,size);
            //插入缓存并设置失效时间
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(lists));
            redisTemplate.expire(redisKey,5,TimeUnit.MINUTES);
        }else{//缓存中有数据
            lists = JSON.parseObject(json,new TypeReference<List<Comment>>(){});
        }
        logger.debug("【lists=】" + lists);
        return lists;
    }

    private List<Comment> doGetHouseComments(Long houseId, Integer size) {
        logger.debug("【houseId=】" + houseId + "【，size=】" + size);

        List<Comment> comments = commentMapper.selectComments(houseId, size);
        logger.debug("【comments=】" + comments);
        comments.forEach( comment -> {
            User user = userDao.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }
}
