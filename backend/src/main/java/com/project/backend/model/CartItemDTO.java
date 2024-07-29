package com.project.backend.model;

import java.util.List;

public class CartItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private List<String> imageUrls;
    private double price;

    // Constructor
//    public CartItemDTO(Long productId, String productName, int quantity, List<String> imageUrls) {
//        this.productId = productId;
//        this.productName = productName;
//        this.quantity = quantity;
//        this.imageUrls = imageUrls;
//    }
    public CartItemDTO() {
    	
    }
    // Getters và setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
    
}

