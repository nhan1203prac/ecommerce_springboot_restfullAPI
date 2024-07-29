package com.project.backend.model;

public class OrderRequest {
    private Long userId;
    private String address;
    private String country;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

    // Getters and setters
    // Constructors
    
}
