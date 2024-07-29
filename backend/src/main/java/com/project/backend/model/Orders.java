package com.project.backend.model;

import java.util.ArrayList;
import java.util.List;

public class Orders {
	private long orderId;
	private String username;
	private int count;
	private double total;
	private String address;
	private String status;
	private List<ProductWithUrlAndCount> product = new ArrayList<>();
	private long userId;
	
	
	

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public List<ProductWithUrlAndCount> getProduct() {
		return product;
	}
	public void setProduct(List<ProductWithUrlAndCount> product) {
		this.product = product;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
