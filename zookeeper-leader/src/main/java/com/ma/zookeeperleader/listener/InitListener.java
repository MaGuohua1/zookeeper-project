package com.ma.zookeeperleader.listener;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class InitListener implements ServletContextListener, IZkDataListener {

	private static final String BASE_LEADER = "/leader";
	private static final String LEADER_NAME = "/innoDB";
	private ZkClient client;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String zkHost = null;
		try {
			zkHost = PropertiesLoaderUtils.loadAllProperties("application.properties")
					.getProperty("spring.data.solr.zk-host");
		} catch (IOException e) {
			e.printStackTrace();
		}
		client = new ZkClient(zkHost);
		if (!client.exists(BASE_LEADER)) {
			client.createPersistent(BASE_LEADER);
		}
		createEphemeral();
		client.subscribeDataChanges(BASE_LEADER + LEADER_NAME, this);
	}

	private void createEphemeral() {
		try {
			client.createEphemeral(BASE_LEADER + LEADER_NAME);
			ElectionMaster.isSurvival = true;
		} catch (Exception e) {
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		client.deleteRecursive(BASE_LEADER + LEADER_NAME);
		client.close();
	}

	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleWithFixedDelay(() -> createEphemeral(), 0, 5, TimeUnit.SECONDS);
	}

}
