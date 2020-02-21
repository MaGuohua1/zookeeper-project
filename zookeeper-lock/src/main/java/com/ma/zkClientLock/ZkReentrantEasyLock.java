package com.ma.zkClientLock;

import java.util.concurrent.CountDownLatch;

public class ZkReentrantEasyLock extends ZkAbstractLock {

	public ZkReentrantEasyLock(String lockName) {
		super(lockName);
	}

	public void unlock() {
		if (client.exists(getLockPath())) {
			client.delete(getLockPath());
			count--;
			if (count == 0) {
				client.close();
			}
		}
	}

	@Override
	public boolean waitLock() {
		boolean flag = false;
		try {
			// 注册监听
			client.subscribeDataChanges(getLockPath(), this);
			if (client.exists(getLockPath())) {
				if (latch == null) {
					latch = new CountDownLatch(1);
				}
				latch.await();// 等待，一直等到接收到事件的通知
			}
			flag = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			client.unsubscribeDataChanges(getLockPath(), this);
		}
		return flag;
	}

	@Override
	public boolean tryLock() {
		try {
			if (!client.exists(getLockPath())) {
				client.createEphemeral(getLockPath());
			}
			count++;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
