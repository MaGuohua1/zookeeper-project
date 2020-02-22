package com.ma.zookeeperconfig.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ma.zookeeperconfig.listener.DataSourceListener;

@Configuration
public class AppConfig {
	@Bean
    public ServletListenerRegistrationBean<DataSourceListener> servletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<DataSourceListener>(new DataSourceListener());
    }
//	@Bean
//    public DruidDataSource dataSource() {
//    	DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//        dataSource.setUsername("spring");
//        dataSource.setPassword("spring");
//        dataSource.setUrl("jdbc:oracle:thin:@192.168.1.104:1521/orclpdb");
//        return dataSource;
//    }
}
