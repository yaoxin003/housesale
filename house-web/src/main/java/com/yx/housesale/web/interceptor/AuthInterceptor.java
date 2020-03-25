package com.yx.housesale.web.interceptor;

import com.google.common.base.Joiner;
import com.yx.housesale.common.constant.HouseConstant;
import com.yx.housesale.common.model.User;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/12/17:55
 */
@Component
@Log4j
public class AuthInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.debug("=======================================");
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((k,v)->{
            if(k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")){
                request.setAttribute(k, Joiner.on(",").join(v));
            }
        });

        String requestURL = request.getRequestURL().toString();
        if(StringUtils.isNotBlank(requestURL) &&
                (requestURL.startsWith("/static") || requestURL.startsWith("/error"))){
            return true;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(HouseConstant.SESSION_USER_ATTRIBUTE);
        if(user != null){
            UserContext.setUser(user);
            log.debug("【user=】" + user);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.remove();
    }
}
