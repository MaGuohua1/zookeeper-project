package com.ma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ma.utils.LoadBalance;
import com.ma.utils.RandomLoadBalance;
import com.ma.vo.Order;
import com.ma.vo.Product;

@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	private RestTemplate restTemplate;
	private LoadBalance balance = new RandomLoadBalance();

	@GetMapping("getOrder/{id}")
	private Order getOrder(@PathVariable("id") int id, HttpServletRequest request) {
		String host = balance.chooseServiceHost();
		String url = "http://" + host + "/product/getProduct/1";
		Product product = restTemplate.getForObject(url, Product.class);
		return new Order(id, "orderName" + request.getLocalPort(), product);
	}
}
