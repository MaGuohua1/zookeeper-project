package com.ma.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.ma.utils.LoadBalance;

public class InitListener implements ServletContextListener, IZkChildListener {

	private static final String base_service = "/service";
	private static final String service_name = "/product";
	private ZkClient client;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String zkHost = PropertiesLoaderUtils.loadAllProperties("application.properties")
					.getProperty("spring.data.solr.zk-host");
			client = new ZkClient(zkHost);
			updateService(client.getChildren(base_service + service_name));
			client.subscribeChildChanges(base_service + service_name, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
		updateService(currentChilds);
	}

	private void updateService(List<String> currentChilds) {
		List<String> list = new ArrayList<String>();
		for (String child : currentChilds) {
			String host = client.readData(base_service + service_name + "/" + child);
			list.add(host);
		}
		LoadBalance.serviceList = list;
	}

}
