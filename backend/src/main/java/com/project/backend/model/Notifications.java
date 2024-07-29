package com.project.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notifications {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
	private String message;
	private String status = "unRead";
	private LocalDateTime currentDateTime = LocalDateTime.now();
	
	
	public Notifications(Long notificationId, String message, String status, LocalDateTime currentDateTime, User user) {
		super();
		this.notificationId = notificationId;
		this.message = message;
		this.status = status;
		this.currentDateTime = currentDateTime;
		this.user = user;
	}

	public Notifications() {

	}

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(LocalDateTime currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
