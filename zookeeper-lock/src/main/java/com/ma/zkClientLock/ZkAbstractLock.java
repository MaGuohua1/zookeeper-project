package com.ma.zkClientLock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.ma.base.AbstractLock;

public abstract class ZkAbstractLock extends AbstractLock implements IZkDataListener {

	private static final String ZK_HOST = "127.0.0.1:2181";
	static final String BASE_LOCK = "/Lock";
	ZkClient client;
	CountDownLatch latch;
	String lockName;
	
	public ZkAbstractLock(String lockName) {
		this.client = new ZkClient(ZK_HOST);
		this.lockName = lockName;
		if (!client.exists(BASE_LOCK)) {
			client.createPersistent(BASE_LOCK, true);
		}
	}

	String getLockPath() {
		if (lockName.startsWith("/")) {
			return BASE_LOCK + lockName;
		} else {
			return BASE_LOCK + "/" + lockName;
		}
	}

	@Override
	public void handleDataChange(String dataPath, Object data) throws Exception {
	}

	@Override
	public void handleDataDeleted(String dataPath) throws Exception {
		if (latch != null) {// 唤醒被等等的线程
			latch.countDown();
		}
	}
}
