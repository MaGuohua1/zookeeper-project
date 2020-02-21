package com.ma.zkClientLock;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkReentrantLock extends ZkAbstractLock {

	private String prevPath;
	private String currentPath;

	public ZkReentrantLock(String lockName) {
		super(lockName);
	}

	@Override
	public void unlock() {
		if (client.exists(currentPath)) {
			client.delete(currentPath);
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
			// 给排在前面的节点添加监听
			client.subscribeDataChanges(prevPath, this);
			if (client.exists(prevPath)) {
				if (latch == null) {
					latch = new CountDownLatch(1);
				}
				latch.await();
				flag = true;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			client.unsubscribeDataChanges(prevPath, this);
		}
		return flag;
	}

	@Override
	public boolean tryLock() {
		// 第一次尝试加锁
		if (currentPath == null || currentPath.isEmpty()) {
			currentPath = client.createEphemeralSequential(getLockPath(), "");
		}
		// 获取当前节点名称
		String currentPathName = currentPath.substring(currentPath.lastIndexOf("/") + 1);
		// 获取所有的临时节点并排序
		List<String> children = client.getChildren(BASE_LOCK);
		List<String> list = new LinkedList<>();
		for (String child : children) {
			if (child.startsWith(lockName)) {
				list.add(child);
			}
		}
		Collections.sort(list);
		// 如果当前节点排名第一，则获取锁成功
		if (currentPathName.equals(list.get(0))) {
			return true;
		}
		// 如果当前节点排名不是第一，则获取前节点
		prevPath = BASE_LOCK + "/" + list.get(Collections.binarySearch(list, currentPathName) - 1);
		return false;
	}
}
