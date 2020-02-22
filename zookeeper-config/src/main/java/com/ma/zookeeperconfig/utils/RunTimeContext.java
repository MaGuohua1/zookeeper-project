package com.ma.zookeeperconfig.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
@Component
public class RunTimeContext implements ApplicationContextAware {

	private static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	public static <T> T getBean(Class<T> cls) {
		return context.getBean(cls);
	}
	
	public static Environment getEnvironment() {
		return context.getEnvironment();
	}

}
