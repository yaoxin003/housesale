package com.yx.housesale.biz.mapper;

import com.yx.housesale.common.model.House;
import com.yx.housesale.common.model.User;
import com.yx.housesale.common.page.PageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/05/9:54
 */
public interface UserMapper extends Mapper<User> {

    public List<User> selectPageUsers(
            @Param("user") User user, @Param("pageParam") PageParam pageParam);

    public int selectPageCount(@Param("user") User user);
}
