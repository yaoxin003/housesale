package com.yx.house.service.apigateway.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @description: spring cloud ribbon 自定义配置
 * @author: yx
 * @date: 2020/02/11/10:17
 */
public class NewRuleConfig {

    @Autowired
    private IClientConfig ribbonClientConfig;

    @Bean
    public IPing ribbonPing(IClientConfig config){
        return new PingUrl(false,"/health");
    }

    @Bean
    public IRule ribbonRule(IClientConfig config){
        //return new RandomRule();
        return new AvailabilityFilteringRule();
    }
}
