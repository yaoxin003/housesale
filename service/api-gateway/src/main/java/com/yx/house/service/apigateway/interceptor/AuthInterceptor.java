package com.yx.house.service.apigateway.interceptor;

import com.google.common.base.Joiner;
import com.yx.house.service.apigateway.common.CommonConstants;
import com.yx.house.service.apigateway.common.UserContext;
import com.yx.house.service.apigateway.dao.UserDao;
import com.yx.house.service.apigateway.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/25/17:59
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String TOKEN_COOKIE = "token";
    @Autowired
    private UserDao userDao;

    Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        logger.info("=======================================【requestURI=】" + requestURI);
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
        //token存入cookie中
        Cookie cookie = WebUtils.getCookie(request,TOKEN_COOKIE);
        this.setCookie1InHeader(request,TOKEN_COOKIE);
        if(cookie != null && StringUtils.isNoneBlank(cookie.getValue())){
            logger.info("【AuthInterceptor.cookie=】" + cookie + "【，cookie.value=】" + cookie.getValue());
            User user = userDao.getUserByToken(cookie.getValue());
            logger.info("【user=】" + user );
           if(user != null){
               request.setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE,user);
               UserContext.setUser(user);
           }
        }
        return true;
    }

    //将cookie1设置到header中
    private void setCookie1InHeader(HttpServletRequest request,String token) {
        Cookie cookie = WebUtils.getCookie(request,token);
        if(cookie == null && null != request.getHeader("Cookie1")){
            String value = StringUtils.substringAfterLast(
                    request.getHeader("Cookie1"),"=");
            cookie = new Cookie("token",value);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String requestURI = request.getRequestURI();
        logger.info("****************postHandle****************************【requestURI=】" + requestURI);
        if(requestURI.startsWith("/static") || requestURI.startsWith("/error")){
            return;
        }
        User user = UserContext.getUser();
        if(user != null && StringUtils.isNoneBlank(user.getToken())){
            String token = requestURI.startsWith("logout") ? "" : user.getToken();
            Cookie cookie = new Cookie(TOKEN_COOKIE,token);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        UserContext.remove();
    }
}
