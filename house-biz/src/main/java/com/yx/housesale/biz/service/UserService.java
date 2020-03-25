package com.yx.housesale.biz.service;

import com.yx.housesale.common.model.User;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import java.util.List;

public interface UserService {

    public List<User> getAllUsers();

    public List<User> getUsers(User user);

    public boolean addAccount(User user);

    public boolean enable(String key);

    public int updateEnableByEmail(String email);

    public User auth(String email,String passwd);

    public int updateUserById(User user);

    public User getAgentById(Integer userId);

    public PageData<User> getAgentList(User query, PageParam build);
}
