package com.yx.housesale.biz.service;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/10/14:17
 */
public interface MailService {

    public void registerNotify(String toEmail);

    public String enable(String key);


    public void sendMail(String subject,String content,String toEmail);
}
