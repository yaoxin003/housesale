package com.yx.house.service.comment.common;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/15:28
 */
public enum RestCode {
    OK(0,"OK"),
    UNKNOWN_ERROR(1,"用户服务异常"),
    TOKEN_INVALID(2,"TOKEN失效"),
    USER_NOT_EXIST(3,"用户不存在"),
    WRONG_PAGE(10100,"页码不合法"),
    USER_NOT_FOUND(10101,"用户未找到"),
    ILLEGAL_PARAMS(10102,"参数不合法");

    public final int code;
    public final String msg;

    private RestCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RestCode{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
