package com.ma.zookeeperconfig.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * FileName: MyListener Author: 程序猿不是猴 Date: 2019/12/5 10:51 Description:
 * 监听zk节点变化
 */

public class DataSourceListener implements ServletContextListener {

	private ZkConfigurerCenter zkConfigurerCenter;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("contextInitialized ...");

		try {
			String zkHost = PropertiesLoaderUtils.loadAllProperties("application.properties")
					.getProperty("spring.data.solr.zk-host");
			zkConfigurerCenter = new ZkConfigurerCenter(zkHost, 3000, "/database");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		zkConfigurerCenter.destroy();
	}
}