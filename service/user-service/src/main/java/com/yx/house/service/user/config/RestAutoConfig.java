package com.yx.house.service.user.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/03/14:00
 */
@Configuration
public class RestAutoConfig {

    public static class RestTemplateConfig{

        @Bean
        @LoadBalanced //spring 对restTemplate bean进行定制，加入loadbalance拦截器进行ip:port的替换
        RestTemplate lbRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            template.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
            return template;
        }

        @Bean
        RestTemplate directRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            template.getMessageConverters().add(1,new FastJsonHttpMessageConvert5());
            return template;
        }

        public static class FastJsonHttpMessageConvert5 extends FastJsonHttpMessageConverter4{

            static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

            public FastJsonHttpMessageConvert5(){
                setDefaultCharset(DEFAULT_CHARSET);
                setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,
                        new MediaType("application","*+json")));
            }
        }
    }
}