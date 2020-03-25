package com.yx.house.service.apigateway.service;

import com.google.common.collect.Lists;
import com.yx.house.service.apigateway.dao.UserDao;
import com.yx.house.service.apigateway.model.User;
import com.yx.house.service.apigateway.utils.BeanHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/24/14:04
 */
@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Value("${domain.name}")
    private String domainName;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserDao userDao;

    public boolean isExist(String email) {
        return this.getUser(email) != null;
    }

    private User getUser(String email) {
        User paramUser = new User();
        paramUser.setEmail(email);
        List<User> users = this.getUserByQuery(paramUser);
        if(!users.isEmpty()){
            return users.get(0);
        }
        return null;
    }

    private List<User> getUserByQuery(User paramUser) {
        return userDao.getUserList(paramUser);
    }


    public boolean addAccount(User account) {
        List<MultipartFile> list = new ArrayList<>();
        if(account.getAvatorFile() != null){
            List<String> imgs = fileService.getImgPath(
                    Lists.newArrayList(account.getAvatorFile())
            );
            account.setAvatar(imgs.get(0));
        }
        account.setEnableUrl("http://" + domainName + "/accounts/verify?key=");
        BeanHelper.setDefaultProp(account,User.class);
        userDao.addUser(account);
        return true;
    }

    public User auth(String email, String password) {
        if(StringUtils.isBlank(email) || StringUtils.isBlank(password)){
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswd(password);
        try{
            user = userDao.authUser(user);
        }catch(Exception e){
            return null;
        }
        logger.debug("【user=】" + user);
        return user;
    }

    public void logout(String token) {
        userDao.logout(token);
    }

    public User updateUser(User user) {
        BeanHelper.onUpdate(user);
        return userDao.updateUser(user);
    }
}
