package com.project.backend.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.model.User;



public interface UserRepository extends JpaRepository<User,Long>{
	User findByEmail(String email);
	User findByUsername(String username);

}
