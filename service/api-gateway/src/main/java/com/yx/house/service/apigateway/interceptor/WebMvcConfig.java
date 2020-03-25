package com.yx.house.service.apigateway.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    private Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private AuthActionInterceptor authActionInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.debug("****************************************************");
        registry.addInterceptor(authInterceptor).excludePathPatterns("/static").addPathPatterns("/**");
        List<String> pathPatterns = this.buildPathPatterns();
        registry.addInterceptor(authActionInterceptor).addPathPatterns(
                pathPatterns.toArray(new String[pathPatterns.size()]));
        super.addInterceptors(registry);
    }

    private List<String> buildPathPatterns() {
        List<String> pathPatterns = new ArrayList<>();

        pathPatterns.add("/accounts/profile");
        pathPatterns.add("/accounts/profileSubmit");

        pathPatterns.add("/agency/detail");
//        pathPatterns.add("/agency/agentList");

        pathPatterns.add("/house/list");
        pathPatterns.add("/house/detail");
        pathPatterns.add("/house/bookmark");
        pathPatterns.add("/house/unbookmark");
//        pathPatterns.add("/house/leaveMsg");
//        pathPatterns.add("/house/toAdd");
//        pathPatterns.add("/house/add");
//        pathPatterns.add("/house/ownList");

        pathPatterns.add("/blog/list");
        pathPatterns.add("/blog/detail");
        return pathPatterns;
    }

    //全局支持CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")//放行哪些域名
                .allowCredentials(true)//是否允许发送cookie信息
                .allowedMethods("GET","POST","PUT","DELETE")//放行哪些HTTP方法
                .allowedHeaders("*")//用于预检请求，放行哪些header
                .exposedHeaders("Header1","Header2");//暴露哪些头部信息
    }
}
