package com.ma.zookeeperleader.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ma.zookeeperleader.listener.ElectionMaster;

@RestController
public class IndexConroller {

	@GetMapping("getServiceInfo")
	private String getServiceInfo() {
		return ElectionMaster.isSurvival? "当前节点是主节点":"当前节点是从节点";
	}
}
