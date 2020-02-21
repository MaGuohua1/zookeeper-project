package com.ma.test;

import com.ma.base.Lock;
import com.ma.zkClientLock.ZkReentrantLock;

public class Test {

	
	public static void main(String[] args) {
		Lock lock = new ZkReentrantLock("aa");
		for (int i = 0; i < 50; i++) {
			new Thread(new Task()).start();
		}
		name(lock);
	}
	
	public static void name(Lock lock) {
		lock.lock();
		System.out.println("aaaaaaaaaaa");
		bbb(lock);
		lock.unlock();
	}

	private static void bbb(Lock lock) {
		lock.lock();
		System.out.println("dddd");
		lock.unlock();
	}
}