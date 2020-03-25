package com.yx.house.service.apigateway.interceptor;

import com.google.common.base.Joiner;
import com.yx.house.service.apigateway.common.UserContext;
import com.yx.house.service.apigateway.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/25/17:23
 */
@Component
public class AuthActionInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(AuthActionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        User user = UserContext.getUser();
        logger.debug("----------------preHandle-----------------------");
        if(user == null){
            String msg = URLEncoder.encode("请先登录","utf-8");
            String fullURL = this.asFullURL(request);
            logger.debug("【newURL=】" + fullURL);

            String target = URLEncoder.encode(fullURL,"utf-8");

            if("GET".equalsIgnoreCase(request.getMethod())){
                response.sendRedirect("/accounts/signin?errorMsg=" + msg + "&target=" + target);
                return false;//修复bug,未登录要返回false
            }else{
                response.sendRedirect("/accounts/signin?errorMsg=" + msg);
                return false;//修复bug,未登录要返回false
            }
        }
        return true;
    }

    public String asFullURL(HttpServletRequest request){
        String url = request.getRequestURL().toString();

        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> urlParamMap = new HashMap<>();
        parameterMap.forEach((k,v) -> {
            urlParamMap.put(k,v[0]);
        });
        String urlParams = Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(urlParamMap);
        urlParams = StringUtils.isNotBlank(urlParams) ?  "?"+urlParams : "";

        String fullURL = url + urlParams;
        logger.debug("【fullURL=】" + fullURL);
        return fullURL;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}