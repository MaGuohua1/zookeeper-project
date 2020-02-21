package com.ma.test;

import com.ma.base.Lock;
import com.ma.zkClientLock.ZkReentrantLock;

public class Task implements Runnable {

	private OrderNumCenerator num = new OrderNumCenerator();
	private Lock lock = new ZkReentrantLock("task");

	@Override
	public void run() {
		lock.lock();
		try {
			int number = num.getNumber();
			System.out.println(Thread.currentThread().getName() + " 获取订单： " + number);
		} finally {
			lock.unlock();
		}
	}

}
