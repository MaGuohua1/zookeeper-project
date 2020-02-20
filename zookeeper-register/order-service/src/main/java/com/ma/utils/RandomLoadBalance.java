package com.ma.utils;

import java.util.Random;

public class RandomLoadBalance extends LoadBalance {

	@Override
	public String chooseServiceHost() {
		int index = new Random().nextInt(serviceList.size());
		return serviceList.get(index);
	}

}
