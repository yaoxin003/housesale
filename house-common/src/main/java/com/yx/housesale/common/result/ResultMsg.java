package com.yx.housesale.common.result;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/08/12:58
 */
public class ResultMsg {
    private static final String errorMsgKey = "errorMsg";
    private static final String successMsgKey = "successMsg";
    private String errorMsg;
    private String successMsg;

    public boolean isSuccess() {
        return errorMsg == null;
    }

    public static ResultMsg errorMsg(String errorMsg) {
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setErrorMsg(errorMsg);
        return resultMsg;
    }

    public static ResultMsg successMsg(String successMsg){
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setSuccessMsg(successMsg);
        return resultMsg;
    }

    public Map<String,String> asMap(){
        Map<String,String> map = Maps.newHashMap();
        map.put(errorMsgKey,errorMsg);
        map.put(successMsgKey,successMsg);
        return map;
    }

    public String asUrlParams(){
        Map<String,String> map = asMap();
        Map<String,String> newMap = Maps.newHashMap();
        map.forEach((k,v) -> {
            if(v != null){
                try {
                    newMap.put(k, URLEncoder.encode(v,"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(newMap);
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    @Override
    public String toString() {
        return "ResultMsg{" +
                "errorMsg='" + errorMsg + '\'' +
                ", successMsg='" + successMsg + '\'' +
                '}';
    }
}
