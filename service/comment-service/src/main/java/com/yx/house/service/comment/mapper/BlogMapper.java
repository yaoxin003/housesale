package com.yx.house.service.comment.mapper;

import com.yx.house.service.comment.common.LimitOffset;
import com.yx.house.service.comment.model.Blog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/15:57
 */
public interface BlogMapper extends Mapper<Blog> {
    public List<Blog> selectBlog(@Param("blog") Blog blog,
                                 @Param("pageParams")LimitOffset limitOffset);

    public Long selectBlogCount(Blog query);
}
