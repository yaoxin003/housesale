package com.yx.house.service.apigateway.common;

import com.yx.house.service.apigateway.utils.RestException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Callable;

public class Rests {

    static Logger logger = LoggerFactory.getLogger(Rests.class);

    private Rests(){};

    public interface ResultHandler{
        <T> T handle(T result);
    }

    public static class DefaultHandler implements ResultHandler{
        @Override
        public <T> T handle(T result){
            int code = 1;
            String msg = "";
            try{
                code = (Integer)FieldUtils.readDeclaredField(result,"code",true);
                msg = (String)FieldUtils.readDeclaredField(result,"msg",true);
            }catch(Exception e){
                //ignore
            }
            if(code != 0){
                throw new RestException("Get errorNo" + code + " when execute rest call with errorMsg");
            }
            return result;
        }
    }

    private static DefaultHandler defaultHandler = new DefaultHandler();

    public static String toUrl(String serviceName,String path){
        String url =  "http://" + serviceName + path;
        logger.debug("【url=】" + url);
        return  url;
    }

    public static <T> T exec(Callable<T> callable){
        return exec(callable,defaultHandler);
    }

    private static <T> T exec(Callable<T> callable, DefaultHandler handler) {
        T result = sendReq(callable);
        return handler.handle(result);
    }

    private static <T> T sendReq(Callable<T> callable) {
        T result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new RestException("sendReq error");
        }finally{
            logger.info("result={}",result);
        }
        return result;
    }

}
