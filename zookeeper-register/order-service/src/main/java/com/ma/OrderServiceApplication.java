package com.ma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.ma.listener.InitListener;

@SpringBootApplication
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplete() {
		return new RestTemplate();
	}

	@Bean
	public ServletListenerRegistrationBean<InitListener> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<InitListener>(new InitListener());
	}
}
