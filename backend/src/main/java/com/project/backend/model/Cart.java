package com.project.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long cartId;

	    @ManyToOne
	    @JoinColumn(name = "user_id", referencedColumnName = "userId")
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "product_id")
	    private Product product;

	    private int quantityOfEachProduct=1;
	    private double price;
		public Long getCartId() {
			return cartId;
		}
		public void setCartId(Long cartId) {
			this.cartId = cartId;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Product getProduct() {
			return product;
		}
		public void setProduct(Product product) {
			this.product = product;
		}
		public int getQuantityOfEachProduct() {
			return quantityOfEachProduct;
		}
		public void setQuantityOfEachProduct(int quantityOfEachProduct) {
			this.quantityOfEachProduct = quantityOfEachProduct;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
	    
	    
}
