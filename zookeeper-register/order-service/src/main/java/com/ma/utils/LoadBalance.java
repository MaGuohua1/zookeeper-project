package com.ma.utils;

import java.util.List;

public abstract class LoadBalance {

	public static List<String> serviceList;

	public abstract String chooseServiceHost();
}
