package com.ma.zookeeperleader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.ma.zookeeperleader.listener.InitListener;

@SpringBootApplication
public class ZookeeperLeaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZookeeperLeaderApplication.class, args);
	}

	@Bean
	public ServletListenerRegistrationBean<InitListener> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<InitListener>(new InitListener());
	}
}
