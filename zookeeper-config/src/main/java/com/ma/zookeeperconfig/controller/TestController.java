package com.ma.zookeeperconfig.controller;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private DataSource dataSource;

	@GetMapping("getUser")
	public List<String> getUser() {
		System.out.println(dataSource);
		JdbcTemplate template = new JdbcTemplate(dataSource);
		String sql = "SELECT t.password FROM Sys_User t WHERE t.username='1'";
		List<String> list = template.queryForList(sql, null, String.class);
		return list;
	}
}
