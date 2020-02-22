package com.ma.zookeeperconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZookeeperConfigApplicationTests {

	@Test
	void contextLoads() {
		createZkNode();
		ChangeDataSource();
	}

	private void createZkNode() {
		CuratorFramework client = getClient();
		try {
			if (client.checkExists().forPath("/database/url")==null) 
				client.create().creatingParentsIfNeeded().forPath("/database/url","jdbc:oracle:thin:@192.168.1.104:1521/orclpdb".getBytes());
			if (client.checkExists().forPath("/database/driverClassName")==null) 
				client.create().creatingParentsIfNeeded().forPath("/database/driverClassName","oracle.jdbc.OracleDriver".getBytes());
			if (client.checkExists().forPath("/database/username")==null) 
				client.create().creatingParentsIfNeeded().forPath("/database/username","spring".getBytes());
			if (client.checkExists().forPath("/database/password")==null) 
				client.create().creatingParentsIfNeeded().forPath("/database/password","spring".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ChangeDataSource() {
		CuratorFramework client = getClient();
		List<CuratorOp> list = new ArrayList<CuratorOp>();

		try {
			CuratorOp forPath = client.transactionOp().setData().forPath("/database/username", "spring1".getBytes());
			CuratorOp forPath2 = client.transactionOp().setData().forPath("/database/password", "spring1".getBytes());
			list.add(forPath);
			list.add(forPath2);
			client.transaction().forOperations(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close();
	}

	private CuratorFramework getClient() {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("192.168.1.104:2181")
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		return client;
	}

}
