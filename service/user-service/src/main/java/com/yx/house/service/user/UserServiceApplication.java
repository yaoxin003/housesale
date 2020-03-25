package com.yx.house.service.user;

import com.yx.house.service.user.config.NewRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@RibbonClient(name="user-service",configuration = NewRuleConfig.class)
@MapperScan(basePackages = "com.yx.house.service.user.mapper")//tkmybatis的注解
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
