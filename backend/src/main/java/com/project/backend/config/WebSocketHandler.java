package com.project.backend.config;

import java.util.Optional;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.project.backend.exception.NotFoundUserIdExceotion;
import com.project.backend.model.Notifications;
import com.project.backend.model.User;
import com.project.backend.respository.NotifiRepository;
import com.project.backend.respository.UserRepository;


public class WebSocketHandler extends TextWebSocketHandler {
	
}

