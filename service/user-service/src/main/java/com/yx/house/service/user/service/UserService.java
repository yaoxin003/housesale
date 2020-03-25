package com.yx.house.service.user.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.yx.house.service.user.common.UserException;
import com.yx.house.service.user.mapper.UserMapper;
import com.yx.house.service.user.model.User;
import com.yx.house.service.user.utils.BeanHelper;
import com.yx.house.service.user.utils.HashUtils;
import com.yx.house.service.user.utils.JwtHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tk.mybatis.mapper.entity.Example;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/21/11:33
 */
@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * 1.首先通过缓存获得
     * 2.不存在通过数据库获得用户对象
     * 3.将用户对象写入换粗，设置缓存时间为5分钟
     * 4.返回对象
     * @param id
     * @return
     */
    public User getUserById(Long id) {
        String redisKey = "user:" + id;
        User user = null;
        if(redisTemplate != null){
            String json = redisTemplate.opsForValue().get(redisKey);
           if(Strings.isNullOrEmpty(json)){
               //查询数据库
               User paramUser = new User();
               paramUser.setId(id);
               user = userMapper.selectOne(paramUser);
               if(user != null){
                   //设置图片
                   user.setAvatar(imgPrefix + user.getAvatar());
                   //存入缓存中
                   redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(user));
                   redisTemplate.expire(redisKey,5, TimeUnit.MINUTES);
               }
           }else{
               //json转为User
               user = JSON.parseObject(json,User.class);
           }
        }
        logger.info("【user=】" + user);
        return user;
    }

    public List<User> getUsersByQuery(User user) {
        List<User> users = userMapper.select(user);
        users.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
        return users;
    }

    /**
        * @description:
         * 1.密码加盐加密
         * 2.插入数据
         * 3.发送邮件
        * @author:  YX
        * @date:    2020/02/22 6:04
        * @param: user
        * @param: enableUrl
        * @return: void
        * @throws:
        */
    public void addAccount(User user, String enableUrl) {
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insertSelective(user);
        this.registerNotify(user.getEmail(),enableUrl);
    }

    private void registerNotify(String email, String enableUrl) {
        String randomKey = HashUtils.hashString(email)
                + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey,email);
        String content = enableUrl + "?key=" + randomKey;
        mailService.sendMail("房产平台激活邮件",content,email);
    }

/**
    * @description:
    *1.从缓存中查找数据
     * 2.更新状态
     * 3.从缓存中删除数据
    * @author:  YX
    * @date:    2020/02/22 8:17
    * @param: key
    * @return: boolean
    * @throws:
    */
    public boolean enable(String key) {
        if(redisTemplate != null){
            //1.从缓存中查找数据
            String email = redisTemplate.opsForValue().get(key);
            if(StringUtils.isBlank(email)){
                throw new UserException(UserException.Type.USER_NOT_FOUND,"无效的key");
            }
            //2.更新状态
            this.updateUserEnable(email);
            //3.从缓存中删除数据
            redisTemplate.delete(key);
        }
        return true;
    }

    private void updateUserEnable(String email) {
        User updateUser = new User();
        updateUser.setEnable(1);
        updateUser.setUpdateTime(new Date());
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email",updateUser.getEmail());
        userMapper.updateByExampleSelective(updateUser,example);
    }

/**
    * @description: 鉴权
     * 1.邮件和密码查询
     * 2.生成token
    * @author:  YX
    * @date:    2020/02/22 15:20
    * @param: email
    * @param: passwd
    * @return: com.yx.house.service.user.model.User
    * @throws: 
    */
    public User auth(String email, String passwd) {
        if(StringUtils.isBlank(email) || StringUtils.isBlank(passwd)){
            throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");
        }
        List<User> users = this.getUsersByQuery(this.buildAuthUser(email, passwd));
        if(!users.isEmpty()){
            User user = users.get(0);
            this.onLogin(user);
            return user;
        }
        logger.debug("【users=】" + users);
        throw new UserException(UserException.Type.USER_AUTH_FAIL,"User Auth Fail");
    }

    //生成token并重置超时时间
    private void onLogin(User user) {
        String token = JwtHelper.genToken(ImmutableMap.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "ts", Instant.now().getEpochSecond() + ""
        ));
        logger.debug("【token=】" + token);
        this.renewToken(token,user.getEmail());
        user.setToken(token);
    }

    private String renewToken(String token, String email) {
        redisTemplate.opsForValue().set(email,token);
        redisTemplate.expire(email,30,TimeUnit.MINUTES);
        return token;
    }

    private User buildAuthUser(String email, String passwd) {
        logger.debug("【passwd=】" + passwd);
        String encryPwd = HashUtils.encryPassword(passwd);
        User user = new User();
        user.setEmail(email);
        logger.debug("【encryPwd=】" + encryPwd);
        user.setPasswd(encryPwd);
        user.setEnable(1);
        return user;
    }

    /**
        * @description:
        * 1.验证token
        * 2.在token中获得email
        * 3.在redis中查询email是否过期
        * 4.redis未过期，重新设置过期时间
        * 5.使用email查询数据库获得用户信息
        * 6.将token数据存入用户信息中
        * @author:  YX
        * @date:    2020/02/22 16:45
        * @param: token
        * @return: com.yx.house.service.user.model.User
        * @throws:
        */
    public User getLoginedUserByToken(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        String email = map.get("email");
        Long expire = redisTemplate.getExpire(email);
        if(expire>0){
            this.renewToken(token,email);
            User user = this.getUserByEmail(email);
            user.setToken(token);
            return user;
        }
        throw new UserException(UserException.Type.USER_NOT_LOGIN,"User not login");
    }

    private User getUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        List<User> list = this.getUsersByQuery(user);
        if(!list.isEmpty()){
            return list.get(0);
        }
        throw new UserException(UserException.Type.USER_NOT_FOUND,"User not found for");
    }

    public void invalidate(String token) {
        Map<String,String> map = JwtHelper.verifyToken(token);
        redisTemplate.delete(map.get("email"));
    }

    public User updateUser(User user) {
        if(user == null || (user.getEmail() == null)){
            return null;
        }
        if(Strings.isNullOrEmpty(user.getPasswd())){
            user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        }
        this.updateUserByEmail(user);
        return this.selectUserByEmail(user);
    }

    private User selectUserByEmail(User user) {
        User param = new User();
        param.setEmail(user.getEmail());
        User retUser = userMapper.selectOne(param);
        return retUser;
    }

    private void updateUserByEmail(User user) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email",user.getEmail());
        userMapper.updateByExampleSelective(user,example);
    }
}
