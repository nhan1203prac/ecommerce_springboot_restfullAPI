package com.project.backend.model;

import java.util.ArrayList;
import java.util.List;

public class ProductWithUrlAndCount {
	private long productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private int count;
    private List<String> pictureUrls = new ArrayList<>();

    


	public long getProductId() {
		return productId;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<String> getPictureUrls() {
		return pictureUrls;
	}
	public void setPictureUrls(List<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}
    
}
