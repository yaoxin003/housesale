package com.yx.house.service.house;

import com.yx.house.service.house.config.NewRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@RibbonClient(name="house-service",configuration = NewRuleConfig.class)
@MapperScan(basePackages = "com.yx.house.service.house.mapper")//tkmybatis的注解
public class HouseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseServiceApplication.class, args);
	}

}
