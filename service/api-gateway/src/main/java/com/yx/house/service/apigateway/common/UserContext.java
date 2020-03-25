package com.yx.house.service.apigateway.common;

import com.yx.house.service.apigateway.model.User;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/12/17:52
 */
public class UserContext {
    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(User user){
        USER_HOLDER.set(user);
    }

    public static User getUser(){
        return USER_HOLDER.get();
    }

    public static void remove(){
        USER_HOLDER.remove();
    }

}
