package com.yx.housesale.web.interceptor;

import com.google.common.base.Joiner;
import com.yx.housesale.common.model.User;
import lombok.extern.log4j.Log4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/12/18:11
 */
@Component
@Log4j
public class AuthActionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception{
        User user = UserContext.getUser();
        log.debug("---------------------------------------");
        if(user == null){
            String msg = URLEncoder.encode("请先登录","utf-8");
            String fullURL = this.asFullURL(request);
            log.debug("【newURL=】" + fullURL);

            String target = URLEncoder.encode(fullURL,"utf-8");

            if("GET".equalsIgnoreCase(request.getMethod())){
                response.sendRedirect("/account/signin?errorMsg=" + msg + "&target=" + target);
                return false;//修复bug,未登录要返回false
            }else{
                response.sendRedirect("/account/signin?errorMsg=" + msg);
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
        log.debug("【fullURL=】" + fullURL);
        return fullURL;
    }
}
