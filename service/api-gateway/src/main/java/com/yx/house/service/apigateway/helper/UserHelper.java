package com.yx.house.service.apigateway.helper;

import com.yx.house.service.apigateway.common.ResultMsg;
import com.yx.house.service.apigateway.model.User;
import org.apache.commons.lang.StringUtils;
/**
 * @description:
 * @author: yx
 * @date: 2020/01/08/13:13
 */
public class UserHelper {
    public static ResultMsg validate(User user) {
        if(user == null){
            return ResultMsg.errorMsg("用户数据为null");
        }
        if(StringUtils.isBlank(user.getName())){
            return ResultMsg.errorMsg("姓名有误");
        }
        if(StringUtils.isBlank(user.getEmail())){
            return ResultMsg.errorMsg("Email有误");
        }
        if(StringUtils.isBlank(user.getPasswd())
                || StringUtils.isBlank(user.getConfirmPasswd())
                || !user.getPasswd().equals(user.getConfirmPasswd())){
            return ResultMsg.errorMsg("密码有误");
        }
        if(user.getPasswd().length() < 6){
            return ResultMsg.errorMsg("密码应大于等于6位");
        }
        return ResultMsg.successMsg("");
    }
}
