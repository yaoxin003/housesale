package com.yx.house.service.comment;

import com.yx.house.service.comment.config.NewRuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@RibbonClient(name="comment-service",configuration = NewRuleConfig.class)
@MapperScan(basePackages = "com.yx.house.service.comment.mapper")//tkmybatis的注解
public class CommentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentServiceApplication.class, args);
	}

}
