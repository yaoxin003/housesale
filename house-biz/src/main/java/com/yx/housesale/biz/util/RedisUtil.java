package com.yx.housesale.biz.util;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description:
 * @author: yx
 * @date: 2020/01/20/12:35
 */
@Log4j
public class RedisUtil {


    private JedisPool jedisPool;

    private static final int MAX_IDLE = 200; //最大空闲连接数
    private static final int MAX_TOTAL = 1024; //最大连接数
    private static final boolean BLOCK_WHEN_EXHAUSTED = true;
    private static final long MAX_WAIT_MILLIS = 10*1000; //建立连接最长等待时间
    private static final boolean TEST_ON_BORROW = true;
    private static final int TIME_OUT = 20*1000;


    public void initPool(String host,int port,int database){
        JedisPoolConfig poolConfig = this.initPoolConfig(host,port,database);
        jedisPool = new JedisPool(poolConfig,host,port,TIME_OUT);
    }

    private JedisPoolConfig initPoolConfig(String host,int port,int database){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(MAX_IDLE);
        poolConfig.setMaxIdle(MAX_TOTAL);
        poolConfig.setBlockWhenExhausted(BLOCK_WHEN_EXHAUSTED);
        poolConfig.setMaxWaitMillis(MAX_WAIT_MILLIS);
        poolConfig.setTestOnBorrow(TEST_ON_BORROW);
        return poolConfig;
    }

    public Jedis getJedis() {
        Jedis jedis  = null;
        try{
            jedis  = jedisPool.getResource();
        }catch(Exception e){
            log.error(e);
        }
        return jedis;
    }
}
