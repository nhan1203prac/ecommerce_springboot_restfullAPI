package com.project.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private long productId;
    
    private String name;
    private String description;
    private double price;
    private String category;
    
    // Khởi tạo danh sách hình ảnh khi tạo mới đối tượng Product
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
//    @JsonBackReference
    private List<Picture> pictures = new ArrayList<>();
    
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL) 
//    @JsonBackReference
   	private List<DetailOrder> detailOrders=new ArrayList<>();
    
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
//    @JsonBackReference
    private List<Cart> cart=new ArrayList<>(); 
    
	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

	public long getProductId() {
		return productId;
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

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public List<DetailOrder> getDetailOrders() {
		return detailOrders;
	}

	public void setDetailOrders(List<DetailOrder> detailOrders) {
		this.detailOrders = detailOrders;
	}
    
    
	
	
}
