package com.project.backend.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;

import com.project.backend.exception.NotFoundOrderIdException;
import com.project.backend.exception.NotFoundUserIdExceotion;
import com.project.backend.model.Cart;
import com.project.backend.model.CheckUserId;
import com.project.backend.model.DetailOrder;
import com.project.backend.model.Notifications;
import com.project.backend.model.Order;
import com.project.backend.model.OrderRequest;
import com.project.backend.model.Orders;
import com.project.backend.model.Picture;
import com.project.backend.model.Product;
import com.project.backend.model.ProductWithUrl;
import com.project.backend.model.ProductWithUrlAndCount;
import com.project.backend.model.User;
import com.project.backend.respository.CartRespository;
import com.project.backend.respository.DetailOrderRepository;
import com.project.backend.respository.NotifiRepository;
import com.project.backend.respository.OrderRespository;
import com.project.backend.respository.ProductRespository;
import com.project.backend.respository.UserRepository;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import jakarta.transaction.Transactional;

@RestController
@Transactional
@CrossOrigin("http://localhost:3001")
public class OrderController {
	@Autowired
	private OrderRespository orderRespository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRespository cartRepository;
	@Autowired
	private DetailOrderRepository detailOrderRepository;
	@Autowired
	private ProductRespository productRespository;
	@Autowired
	private NotifiRepository notificationRespository;
	@PostMapping("/orders")
	ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
		Long userId = orderRequest.getUserId();
	    String address = orderRequest.getAddress();
	    String country = orderRequest.getCountry();
		Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
	    user.setCount(0);
		List<Cart> userItemCart = cartRepository.findByUserUserId(userId);
		
		Order order = new Order();
		order.setAddress(address);
		order.setCountry(country);
		LocalDate currentDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currentDate);
		order.setDate(sqlDate);
		order.setUser(user);
		
//		Order savedOrder = orderRespository.save(order);
		for (Cart cart : userItemCart) {
			DetailOrder detailOrder = new DetailOrder();
			detailOrder.setOrder(order);
			detailOrder.setProduct(cart.getProduct()); 
	        detailOrder.setQuantity(cart.getQuantityOfEachProduct()); 
	        order.setTotal(detailOrder.getQuantity() * detailOrder.getProduct().getPrice());
	        
	     
	        detailOrderRepository.save(detailOrder);
	        orderRespository.save(order);
		}
		int deletedCart = cartRepository.deleteByUserUserId(userId);
		
		
		List<User> users = userRepository.findAll();
		for (User user2 : users) {
			if (user2.isAdmin()==true) {
				Notifications notifi = new Notifications();
				notifi.setStatus("unread");
				notifi.setCurrentDateTime(LocalDateTime.now());
				notifi.setMessage("New order from "+ user.getUsername());
				notifi.setUser(user2);
				notificationRespository.save(notifi);
			}
		}
		
		
		
	   

     
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/orders")
	ResponseEntity<?> getOrders(){
		List<Order> orders = orderRespository.findAll();
		List<Orders> getOrders = new ArrayList<>();
		for (Order order : orders) {
			Orders od = new Orders();
			od.setOrderId(order.getOrderId());
			od.setAddress(order.getAddress());
			od.setStatus(order.getStatus());
			
			Optional<User> optionalUser = userRepository.findById(order.getUser().getUserId());
			User user = optionalUser.orElseThrow(()->new NotFoundUserIdExceotion(order.getUser().getUserId()));
			od.setUsername(user.getUsername());
			
			od.setTotal(order.getTotal());
			od.setUserId(order.getUser().getUserId());
			int count =0;
			List<DetailOrder> detailorder = detailOrderRepository.findByOrderOrderId(od.getOrderId());
			
			List<ProductWithUrlAndCount> productWithUrlAndCount = new ArrayList<>();
			for (DetailOrder dtOrder : detailorder) {
				count+=dtOrder.getQuantity();
				
				Product product = productRespository.findByProductId(dtOrder.getProduct().getProductId());
				
				ProductWithUrlAndCount productWithUrl = new ProductWithUrlAndCount();
	            productWithUrl.setProductId(product.getProductId());
	            productWithUrl.setName(product.getName());
	            productWithUrl.setDescription(product.getDescription());
	            productWithUrl.setPrice(product.getPrice());
	            productWithUrl.setCategory(product.getCategory());
	            productWithUrl.setCount(dtOrder.getQuantity());
	            List<String> pictureUrls = new ArrayList<>();
	            for (Picture picture : product.getPictures()) {
	                pictureUrls.add(picture.getUrl());
	            }
	            productWithUrl.setPictureUrls(pictureUrls);
	            productWithUrlAndCount.add(productWithUrl);
			}
			
			od.setProduct(productWithUrlAndCount);
			od.setCount(count);
			getOrders.add(od);
		}
		return ResponseEntity.ok(getOrders);
				
	}
	
	@PatchMapping("/orders/{orderId}/mark-shipped")
	ResponseEntity<?> MarkShipped(@PathVariable("orderId") Long orderId,@RequestBody CheckUserId userId){
		Optional<Order> optionalOrder = orderRespository.findById(orderId);
		Order order = optionalOrder.orElseThrow(()->new NotFoundOrderIdException(orderId));
		order.setStatus("shipped");
		Optional<User> optionalUser = userRepository.findById(userId.getUserId());
		User user = optionalUser.orElseThrow(()->new NotFoundUserIdExceotion(userId.getUserId()));
		Notifications notifi = new Notifications();
		notifi.setMessage("Order " + orderId + " shipped with success");
		notifi.setCurrentDateTime(LocalDateTime.now());
		notifi.setStatus("unread");
		notifi.setUser(user);
		notificationRespository.save(notifi);
		return getOrders();
	}
	
	@GetMapping("/users/notifications/{userId}")
	List<?> getNotification(@PathVariable("userId") Long userId){
		List<Notifications> notifications = notificationRespository.findByUserUserId(userId);
		
		return notifications;
	}
	
	@PostMapping("/users/notifications/{userId}")
	List updateNotification(@PathVariable("userId")Long userId) {
		List<Notifications> notifications = notificationRespository.findByUserUserId(userId);
		for (Notifications notifications2 : notifications) {
			notifications2.setStatus("read");
			notificationRespository.save(notifications2);
		}
		return notifications;
	}
	
}
