package com.yx.housesale.biz.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.yx.housesale.biz.mapper.UserMapper;
import com.yx.housesale.biz.service.MailService;
import com.yx.housesale.common.model.User;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/10/14:18
 */
@Log4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${domain.name}")
    private String domainName;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserMapper userMapper;

    private final Cache<String,String> registerCache = CacheBuilder.newBuilder().maximumSize(100)
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    User u = new User();
                    u.setEmail(notification.getValue());
                    userMapper.delete(u);
                }
            }).build();

    @Async
    public void registerNotify(String toEmail){
        String randomKey = RandomStringUtils.randomAlphabetic(10);
        registerCache.put(randomKey,toEmail);
        String url = "http://" + domainName + "/account/verify?key="+ randomKey;
        log.debug("【email.url=】"+ url);
        this.sendMail("房产平台用户激活邮件",url,toEmail);
    }

    /**
        * @description: 验证邮箱
        * @author:  YX
        * @date:    2020/01/11 12:57
        * @param: key
        * @return: boolean
        * @throws: 
        */
    @Override
    public String enable(String key) {
        String email = registerCache.getIfPresent(key);
        log.debug("【cache.email=】" + email);
        if(StringUtils.isBlank(email)){
            return null;
        }
       // registerCache.invalidate(key);
        return email;
    }

    public void sendMail(String subject,String content,String toEmail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail);
        message.setText(content);
        message.setSubject(subject);
        mailSender.send(message);
        log.debug("【subject=】" + subject + ",【content=】" + content
                + "【toEmail=】" + toEmail + "，邮件发送成功");
    }


}
