package com.yx.housesale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.yx.housesale.biz.mapper")
@EnableAsync
public class HouseWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseWebApplication.class, args);
	}

}
