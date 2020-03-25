package com.yx.house.service.comment.utils;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/26/15:52
 */
public class RestException extends RuntimeException {

    public RestException(String errorCode) {
        super(errorCode);
    }

    public RestException(Throwable cause){
        super(cause);
    }

    public RestException(String errorCode, String errorMsg){
        super(errorCode + ":" + errorMsg);
    }
}
