package com.yx.house.service.user.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:全局异常处理
 * @author: yx
 * @date: 2020/02/14/10:40
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public RestResponse<Object> handler(HttpServletRequest req, Throwable throwable){
        LOGGER.error("全局异常处理");
        LOGGER.error(throwable.getMessage(),throwable);
        Object target = throwable;
        RestCode restCode = Exception2CodeRepo.getCode(throwable);
        LOGGER.info("restCode=" +restCode);
        RestResponse<Object> response = new RestResponse<Object>(restCode.code,restCode.msg);
        LOGGER.info("response=" +response);
        return response;
    }
}
