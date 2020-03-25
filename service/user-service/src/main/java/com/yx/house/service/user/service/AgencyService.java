package com.yx.house.service.user.service;

import com.yx.house.service.user.common.PageParams;
import com.yx.house.service.user.controller.UserController;
import com.yx.house.service.user.mapper.AgencyMapper;
import com.yx.house.service.user.mapper.UserMapper;
import com.yx.house.service.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/23/12:49
 */
@Service
public class AgencyService {

    Logger logger = LoggerFactory.getLogger(AgencyService.class);

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private UserMapper userMapper;

    public Pair<List<User>,Long> getAllAgent(PageParams pageParams) {
        List<User> agents = agencyMapper.selectAgent(new User(),pageParams);
        setImg(agents);
        Long count = this.selectAgentCount(new User());
        return ImmutablePair.of(agents,count);
    }

    private void setImg(List<User> users){
        users.forEach( u -> {
            u.setAvatar( imgPrefix + u.getAvatar());
        });
    }

    public Long selectAgentCount(User user){
        Integer count = 0;
        if(user != null){
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria().andEqualTo("enable", 1)
                    .andEqualTo("type", 2);
            if(StringUtils.isNotBlank(user.getName())){
                criteria.andEqualTo("email",user.getEmail());
            }
            count = userMapper.selectCountByExample(example);
        }
        return count.longValue();
    }

    public User getAgentDetail(Long id) {
        User user = new User();
        user.setId(id);
        user.setEnable(1);
        user.setType(2);
        List<User> users = agencyMapper.selectAgent(user, new PageParams(1, 1));
        setImg(users);
        if(users.isEmpty()){
            return null;
        }
        User resUser = users.get(0);
        logger.info("【resUser=】" + resUser);
        return resUser;
    }

}
