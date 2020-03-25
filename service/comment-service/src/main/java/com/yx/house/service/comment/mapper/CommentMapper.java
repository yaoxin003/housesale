package com.yx.house.service.comment.mapper;

import com.yx.house.service.comment.model.Comment;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/03/04/15:32
 */
public interface CommentMapper extends Mapper<Comment> {
   public List<Comment> selectComments(@Param("houseId")Long houseId,
                                 @Param("size") Integer size);

    public List<Comment> selectBlogComments(@Param("blogId") Long blogId,
                                     @Param("size") Integer size);
}
