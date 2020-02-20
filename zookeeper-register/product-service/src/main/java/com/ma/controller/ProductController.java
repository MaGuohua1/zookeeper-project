package com.ma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ma.vo.Product;

@Controller
@RequestMapping("product")
public class ProductController {
	@ResponseBody
	@GetMapping("getProduct/{id}")
	private Product getProduct(@PathVariable("id") int id,HttpServletRequest request) {
		return new Product(id, "西瓜"+request.getLocalPort(), 20d);
	}
}
