package com.yx.housesale.biz.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.yx.housesale.biz.mapper.UserMapper;
import com.yx.housesale.biz.service.FileService;
import com.yx.housesale.biz.service.MailService;
import com.yx.housesale.biz.service.UserService;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.model.House;
import com.yx.housesale.common.model.User;
import com.yx.housesale.common.page.PageData;
import com.yx.housesale.common.page.PageParam;
import com.yx.housesale.common.page.Pagination;
import com.yx.housesale.common.utils.BeanHelper;
import com.yx.housesale.common.utils.HashUtils;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/05/10:04
 */
@Log4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService fileService;
    @Autowired
    private MailService mailService;

    public List<User> getAllUsers(){
        return userMapper.selectAll();
    }

    /**
        * @description: 保存图片，插入数据，发送邮件
        * @author:  YX
        * @date:    2020/01/09 18:26
        * @param: user
        * @return: boolean
        * @throws:
        */
    @Override
    public boolean addAccount(User user) {
        log.debug("【before add user=】"+ user);
        this.buildInsertAccount(user);
        log.debug("【after add user=】"+ user);
        userMapper.insert(user);
        mailService.registerNotify(user.getEmail());
        return true;
    }

    /**
     * 使用邮箱激活来码激活用户
     * @param key
     * @return
     */
    public boolean enable(String key){
        String email = mailService.enable(key);
        if(StringUtils.isBlank(email)){
            return false;
        }
        int count = this.updateEnableByEmail(email);
        if(count != 1){
            return false;
        }
        return true;
    }

    /**
        * @description: 使用email更新用户为可用。
        * @author:  YX
        * @date:    2020/01/11 13:06
        * @param: email
        * @return: int
        * @throws:
        */
    public int updateEnableByEmail(String email){
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email",email);
        User user = new User();
        user.setEnable(HouseConstant.DB_HOUSE_USER_ENABLE_START);
        int res = userMapper.updateByExampleSelective(user,example);
        log.debug("【res=】" + res);
        return res;
    }

/**
    * @description: 鉴权
    * @author:  YX
    * @date:    2020/01/12 9:54
    * @param: email
    * @param: passwd
    * @return: com.yx.housesale.common.model.User
    * @throws: 
    */
    public User auth(String email,String passwd){
        log.debug("email=" + email + ",passwd=" + passwd);
        String passwdSALTMD5 = HashUtils.encryPassword(passwd);
        User user = new User();
        user.setEmail(email);
        user.setPasswd(passwdSALTMD5);
        user.setEnable(HouseConstant.DB_HOUSE_USER_ENABLE_START);
        User dbUser = null;
        List<User> users = this.getUsers(user);
        if(users != null && !users.isEmpty()){
            dbUser = users.get(0);
        }
        log.debug("【dbUser=】" + dbUser);
        return dbUser;
    }
    
    public List<User> getUsers(User user){
        List<User> users = userMapper.select(user);
        if(users != null && !users.isEmpty()){
           users.forEach( u -> {
               u.setAvatar(imgPrefix + u.getAvatar());
           });
        }
        return users;
    }
        
    /**
        * @description: 设置加盐密码，上传图片，默认值
        * @author:  YX
        * @date:    2020/01/10 13:49
        * @param: user
        * @return: void
        * @throws:
        */
    private void buildInsertAccount(User user) {
        user.setEnable(HouseConstant.DB_HOUSE_USER_ENABLE_STOP);//0:停用
        String md5Passwd = HashUtils.encryPassword(user.getPasswd());
        user.setPasswd(md5Passwd);
        //上传头像图片
        List<MultipartFile> partFiles = new ArrayList<>();
        partFiles.add(user.getAvatorFile());
        List<String> imgPaths = fileService.getImgPath(partFiles);
        if(!imgPaths.isEmpty()){
            user.setAvatar(imgPaths.get(0));
        }
        //设置默认值
        BeanHelper.setDefaultProp(user,User.class);
        BeanHelper.onInsert(user);
    }

    public int updateUserById(User user){
        log.debug("【user=】" + user);
        user.setUpdateTime(new Date());
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id",user.getId());
        int i = userMapper.updateByExampleSelective(user, example);
        log.debug("【i=】" + i);
        return i;
    }

    @Override
    public User getAgentById(Integer userId){
        User retUser = null;
        User paramUser = new User();
        paramUser.setId(userId.longValue());
        paramUser.setType(HouseConstant.DB_HOUSE_USER_TYPE_AGENT);
        paramUser.setEnable(HouseConstant.DB_HOUSE_USER_ENABLE_START);
        List<User> users = this.getUsers(paramUser);
        if(!users.isEmpty()){
            retUser = users.get(0);
        }
        log.debug("【retUser=】" + retUser);
        return retUser;
    }

    public PageData<User> getAgentList(User query, PageParam pageParam){
        this.buildQueryAgentParam(query);
        List<User> userList = this.getAgentListAndSetImg(query, pageParam);
        int count = userMapper.selectPageCount(query);
        log.debug("【count=】" + count);
        return PageData.buildPage(userList,count,pageParam.getPageSize(),pageParam.getPageNum());
    }

    private List<User> getAgentListAndSetImg(User query, PageParam pageParam) {
        List<User> userList = userMapper.selectPageUsers(query, pageParam);
        log.debug("【userList=】" + userList);
        if(!userList.isEmpty()){
            userList.forEach(u->{
                u.setAvatar(imgPrefix + u.getAvatar());
            });

        }
        return userList;
    }

    private void buildQueryAgentParam(User query) {
        query.setType(HouseConstant.DB_HOUSE_USER_TYPE_AGENT);
    }

}
