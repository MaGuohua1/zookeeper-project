package com.ma.listener;

import java.io.IOException;
import java.net.InetAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.ma.register.ServiceRegister;

public class InitListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String port = PropertiesLoaderUtils.loadAllProperties("application.properties").getProperty("server.port");
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			String host = hostAddress + ":" + port;
			ServiceRegister.register(host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
