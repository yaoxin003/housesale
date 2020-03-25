package com.yx.house.service.user.mapper;

import com.yx.house.service.user.common.PageParams;
import com.yx.house.service.user.model.Agency;
import com.yx.house.service.user.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/23/13:24
 */
public interface AgencyMapper extends Mapper<Agency> {

    public List<User> selectAgent(@Param("user") User user, @Param("pageParams")PageParams pageParams);

}
