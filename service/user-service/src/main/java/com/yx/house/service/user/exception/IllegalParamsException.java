package com.yx.house.service.user.exception;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/14/10:18
 */
public class IllegalParamsException extends RuntimeException
        implements WithTypeException{

    public enum Type{
        WRONG_PAGE_NUM,
        WRONG_TYPE
    }

    private Type type;

    public IllegalParamsException(Type type,String msg){
        super(msg);
        this.type = type;
    }


    public Type type(){
        return this.type;
    }

}
