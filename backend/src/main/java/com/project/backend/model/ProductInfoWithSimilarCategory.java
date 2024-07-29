package com.project.backend.model;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoWithSimilarCategory {
	 private ProductWithUrl productWithId;
	 private List<ProductWithUrl> productsWithSameCategory = new ArrayList<>();
	public ProductWithUrl getProductWithId() {
		return productWithId;
	}
	public void setProductWithId(ProductWithUrl productWithId) {
		this.productWithId = productWithId;
	}
	public List<ProductWithUrl> getProductsWithSameCategory() {
		return productsWithSameCategory;
	}
	public void setProductsWithSameCategory(List<ProductWithUrl> productsWithSameCategory) {
		this.productsWithSameCategory = productsWithSameCategory;
	}
	 
}
