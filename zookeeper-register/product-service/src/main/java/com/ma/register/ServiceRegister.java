package com.ma.register;

import java.io.IOException;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ServiceRegister {

	private static final String BASE_SERVICE = "/service";
	private static final String SERVICE_NAME = "/product";

	public static void register(String host) {
		try {
			String zk_host = PropertiesLoaderUtils.loadAllProperties("application.properties")
					.getProperty("spring.data.solr.zk-host");
			String servicePath = BASE_SERVICE + SERVICE_NAME;
			ZkClient client = new ZkClient(zk_host, 3000, 3000);
			if (!client.exists(servicePath)) {
				client.createPersistent(servicePath, true);
			}
			client.createEphemeralSequential(servicePath + "/child", host);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
