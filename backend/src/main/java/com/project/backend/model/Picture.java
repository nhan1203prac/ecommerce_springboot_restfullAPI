package com.project.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pictures")
public class Picture {
	@Id
	@GeneratedValue
	private long pictureId;
	@ManyToOne
    @JoinColumn(name = "product_id") // Tên cột trong bảng Order để lưu trữ userId
    private Product product;
	private String url;
	public long getPictureId() {
		return pictureId;
	}
	public void setPictureId(long pictureId) {
		this.pictureId = pictureId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
