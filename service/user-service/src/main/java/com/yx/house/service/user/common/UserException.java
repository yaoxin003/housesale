package com.yx.house.service.user.common;

import com.yx.house.service.user.exception.WithTypeException;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/22/8:40
 */
public class UserException extends RuntimeException implements WithTypeException{

    private Type type;

    public Type type(){
        return type;
    }

    public enum Type{
        USER_NOT_LOGIN, USER_NOT_FOUND, USER_AUTH_FAIL;
    }

    public UserException(Type type,String message){
        super(message);
        this.type = type;
    }
}
