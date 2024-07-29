package com.project.backend.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.Cart;
import com.project.backend.model.Notifications;

public interface NotifiRepository  extends JpaRepository<Notifications, Long>{
	List<Notifications> findByUserUserId(long userId);

}
