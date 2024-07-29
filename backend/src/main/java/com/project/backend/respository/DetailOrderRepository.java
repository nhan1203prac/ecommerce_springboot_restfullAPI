package com.project.backend.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.DetailOrder;

public interface DetailOrderRepository extends JpaRepository<DetailOrder, Long>{
	List<DetailOrder> findByOrderOrderId(Long orderId);
}
