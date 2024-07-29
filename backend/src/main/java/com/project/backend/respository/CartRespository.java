package com.project.backend.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.Cart;
import com.project.backend.model.User;


public interface CartRespository extends JpaRepository<Cart, Long> {
	List<Cart> findByUserUserId(long userId);
	Cart findByUserUserIdAndProductProductId(Long userId, Long productId);
	int deleteByUserUserId(Long userId);
}