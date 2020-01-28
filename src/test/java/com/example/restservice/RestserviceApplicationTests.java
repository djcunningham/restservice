package com.example.restservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RestserviceApplicationTests {

	@Autowired
	private UserController controller;


	@Test
	void contextLoads() {
			assertThat(controller).isNotNull();
	}

}
