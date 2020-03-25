package com.yx.house.service.user.common;

import com.google.common.collect.ImmutableMap;
import com.yx.house.service.user.exception.IllegalParamsException;
import com.yx.house.service.user.exception.WithTypeException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.reflect.FieldUtils;

/**
 * @description: 存查异常到编码的映射
 * @author: yx
 * @date: 2020/02/14/10:45
 */
public class Exception2CodeRepo {

    private static final ImmutableMap<Object,RestCode> MAP =
            ImmutableMap.<Object,RestCode>builder()
                    .put(IllegalParamsException.Type.WRONG_PAGE_NUM,RestCode.WRONG_PAGE)
                    .put(UserException.Type.USER_NOT_LOGIN,RestCode.TOKEN_INVALID)
                    .put(IllegalParamsException.class, RestCode.UNKNOWN_ERROR)
            .build();

    public static RestCode getCode(Throwable throwable) {
        if(throwable == null){
            return RestCode.UNKNOWN_ERROR;
        }
        Object target = throwable;
        if(throwable instanceof WithTypeException){
            Object type = getType(throwable);
            if(type != null){
                target = type;
            }
        }
        RestCode restCode = MAP.get(target);
        if(restCode != null){
            return restCode;
        }
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if(rootCause != null){
            return getCode(rootCause);
        }
        return RestCode.UNKNOWN_ERROR;
    }

    private static Object getType(Throwable throwable){
        try{
            return FieldUtils.readDeclaredField(throwable,"type",true);
        }catch (Exception e){
            return null;
        }
    }
}
