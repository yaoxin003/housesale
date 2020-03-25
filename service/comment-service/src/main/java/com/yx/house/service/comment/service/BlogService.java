package com.yx.house.service.comment.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yx.house.service.comment.common.LimitOffset;
import com.yx.house.service.comment.mapper.BlogMapper;
import com.yx.house.service.comment.model.Blog;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/06/9:05
 */
@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    public Pair<List<Blog>,Long> queryBlog(Blog blog, LimitOffset build) {
        List<Blog> blogs = blogMapper.selectBlog(blog, build);
        this.populate(blogs);
        Long blogCount = blogMapper.selectBlogCount(blog);
        return ImmutablePair.of(blogs,blogCount);
    }

    private void populate(List<Blog> blogs) {
        if(blogs!=null && !blogs.isEmpty()){
            blogs.stream().forEach( item -> {
                String stripped = Jsoup.parse(item.getContent()).text();
                item.setDigest(stripped.substring(0,Math.min(stripped.length(),40)));
                String tags = item.getTags();
                item.getTagList().addAll(
                        Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
    }

    public Blog queryOneBlog(Integer id) {
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlog(query, LimitOffset.build(1, 0));
        if(blogs != null && !blogs.isEmpty()){
            return blogs.get(0);
        }
        return null;
    }

}
