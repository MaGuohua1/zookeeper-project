package com.ma.zookeeperconfig.listener;

import java.util.List;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.ma.zookeeperconfig.utils.RunTimeContext;

/**
 * zookeeper配置中心类,从zookeeper动态获取数据库配置
 * 
 * @author mgh_2
 *
 * @desription
 */
public class ZkConfigurerCenter implements TreeCacheListener {

	// 客户端
	private CuratorFramework client;
	// 事件监听
	private TreeCache treeCache;
	// zk的IP和客户端
	private String zkHost;
	// 超时设置
	private int sessionTimeout;
	// zk上读取的数据存放位置
	private Properties props;
	// 数据源路径
	private String databasePath;

	public ZkConfigurerCenter(String zkHost, int sessionTimeout, String databasePath) {
		this.zkHost = zkHost;
		this.sessionTimeout = sessionTimeout;
		this.databasePath = databasePath;
		this.props = new Properties();
		// 初始化zk客户端
		initZkClient();
		// 获取数据源数据,放入prop
		getCinfigData();
		// 添加监听
		addZkListener();
	}

	private void initZkClient() {
		client = CuratorFrameworkFactory.builder().connectString(zkHost).sessionTimeoutMs(sessionTimeout)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
	}

	// 从zookeeper的Jdbc节点下获取数据库配置存入props
	private void getCinfigData() {
		try {
			List<String> list = client.getChildren().forPath(databasePath);
			for (String key : list) {
				String value = new String(client.getData().forPath(databasePath + "/" + key));
				if (!StringUtils.isEmpty(value)) {
					props.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 添加监听
	private void addZkListener() {
		try {
			treeCache = new TreeCache(client, databasePath);
			treeCache.getListenable().addListener(this);
			treeCache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	// 对zookeeper上的数据库配置文件所在节点进行监听，如果有改变就动态刷新props
	public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
		if (event.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
			getCinfigData();
			DruidDataSource dataSource = RunTimeContext.getBean(DruidDataSource.class);
			dataSource.restart();
			dataSource.setUrl(props.getProperty("url"));
			dataSource.setDriverClassName(props.getProperty("driverClassName"));
			dataSource.setUsername(props.getProperty("username"));
			dataSource.setPassword(props.getProperty("password"));
		}
	}

	public void destroy() {
		client.close();
		treeCache.close();
	}
}
