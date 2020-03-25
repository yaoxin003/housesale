package com.yx.housesale.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/12/18:19
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private AuthActionInterceptor authActionInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
        List<String> pathPatterns = this.buildPathPatterns();
        registry.addInterceptor(authActionInterceptor).addPathPatterns(pathPatterns);
        super.addInterceptors(registry);
    }

    private List<String> buildPathPatterns() {
        List<String> pathPatterns = new ArrayList<>();
        pathPatterns.add("/account/profile");

        pathPatterns.add("/house/list");
        //pathPatterns.add("/house/detail");
        pathPatterns.add("/house/leaveMsg");
        pathPatterns.add("/house/toAdd");
        pathPatterns.add("/house/add");
        pathPatterns.add("/house/ownList");
        pathPatterns.add("/house/bookmarkList");
        pathPatterns.add("/agency/agentList");
        pathPatterns.add("/agency/detail");
        return pathPatterns;
    }
}
