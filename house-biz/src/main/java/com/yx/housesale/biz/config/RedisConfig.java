package com.yx.housesale.biz.config;

import com.yx.housesale.biz.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/20/12:34
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.cache.host:disabled}")
    private String cacheHost;

    @Value("${spring.redis.cache.port:0}")
    private int cachePort;

    @Value("${spring.redis.cache.database:0}")
    private int cacheDatabase;

    @Bean
    public RedisUtil getRedisUtil(){
        if(cacheHost.equals("disabled")){
            return null;
        }
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initPool(cacheHost,cachePort,cacheDatabase);
        return redisUtil;
    }
}
