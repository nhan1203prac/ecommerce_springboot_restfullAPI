package com.project.backend.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.Order;
import com.project.backend.model.User;

public interface OrderRespository extends JpaRepository<Order, Long>{
	List<Order> findByUser(User user);
}
