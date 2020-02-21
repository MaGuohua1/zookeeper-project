package com.ma.base;

public abstract class AbstractLock implements Lock {
	protected int count;

	public void lock() {
		if (!tryLock()) {
			waitLock();
			lock();
		} else {
			count++;
		}
	}

	public abstract boolean waitLock();

	public abstract boolean tryLock();

}
