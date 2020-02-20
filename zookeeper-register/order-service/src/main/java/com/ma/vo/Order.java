package com.ma.vo;

public class Order {

	private Integer id;
	private String name;
	private Product product;

	public Order() {
	}

	public Order(Integer id, String name, Product product) {
		this.id = id;
		this.name = name;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", product=" + product + "]";
	}
}
