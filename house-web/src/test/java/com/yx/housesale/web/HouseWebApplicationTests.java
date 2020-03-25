package com.yx.housesale.web;

import com.yx.housesale.biz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HouseWebApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
		userService.updateEnableByEmail("yaoxin123@163.com");
	}

}
