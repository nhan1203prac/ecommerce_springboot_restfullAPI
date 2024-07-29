package com.project.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.exception.EmailExistsException;
import com.project.backend.exception.NotFoundUserIdExceotion;
import com.project.backend.exception.UserNotFoundException;
import com.project.backend.model.DetailOrder;
import com.project.backend.model.Order;
import com.project.backend.model.Product;
import com.project.backend.model.User;
import com.project.backend.respository.OrderRespository;
import com.project.backend.respository.UserRepository;




@RestController
@CrossOrigin("http://localhost:3001")
public class UserController {
	@Autowired
	private OrderRespository orderRespository;
	@Autowired
	private UserRepository userRepository;
	 @Autowired
	private PasswordEncoder passwordEncoder;
	 @PostMapping("/users/register")
	 public ResponseEntity<?> createUser(@RequestBody User newUser) {
	     System.out.println("Đang xử lý request đăng ký người dùng");
	     System.out.println(newUser);
	     String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	     Pattern pattern = Pattern.compile(emailRegex);
	     
	     if (!pattern.matcher(newUser.getEmail()).matches()) {
	         System.out.println("Email không hợp lệ: " + newUser.getEmail());
	         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không hợp lệ");
	     }
	     
	     if (userRepository.findByEmail(newUser.getEmail()) != null) {
	         System.out.println("Email đã được sử dụng: " + newUser.getEmail());
	         return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã được sử dụng");
	     }

	     User existingUser = userRepository.findByUsername(newUser.getUsername());
	     if (existingUser != null) {
	         System.out.println("Tên người dùng đã tồn tại: " + newUser.getUsername());
	         return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên người dùng đã tồn tại");
	     } 
	     System.out.println("Đang thực hiện: " + newUser.getUsername());
	     
	     newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//	     newUser.setIsAdmin(false);
	     userRepository.save(newUser);
	     
	     System.out.println("Đăng ký người dùng thành công: " + newUser.getUsername());
	     
	     return ResponseEntity.ok(newUser);
	 }

	
//	User newUser(@RequestBody User newUser) {
//	return userRepository.save(newUser);
//	
//	
//}
	@PostMapping("/users/login")
	public ResponseEntity<User> userLogin(@RequestBody User user) {
	    User existingUser = userRepository.findByUsername(user.getUsername());
	    if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
	        throw new UserNotFoundException("Wrong username or password");
	    }
	    return ResponseEntity.ok(existingUser);
	}
	
//	@GetMapping("/users/{userId}/orders")
//    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("userId") long userId) {
//		 Optional<User> optionalUser  = userRepository.findById(userId);
//		 User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
//        List<Order> orders = orderRespository.findByUser(user);
//
//        return ResponseEntity.ok(orders);
//    }
//	@GetMapping("/user")
//	public ResponseEntity<?> createUser() {
//	   
//	    User newUser = new User();
//	    newUser.setUsername("haizz");
//	    newUser.setPassword("123456"); 
//	    newUser.setEmail("v22t@gmail.com");
////	    newUser.setIsAdmin(false);
//	    
//	    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//	    Pattern pattern = Pattern.compile(emailRegex);
//	    if (!pattern.matcher(newUser.getEmail()).matches()) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không hợp lệ");
//	    }
//	   
//	    if (userRepository.findByEmail(newUser.getEmail()) != null) {
//	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã được sử dụng");
//	    }
////	    Optional<User> existingUser = userRepository.findById(newUser.getUserId());
//	    User existingUser = userRepository.findByUsername(newUser.getUsername());
//	    if (existingUser !=null) {
//	        return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên người dùng đã tồn tại");
//	    }
//
//	   System.out.println("success rồi");
//	    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//	    userRepository.save(newUser);
//
//	    return ResponseEntity.ok(newUser);
//	}
	@GetMapping("/users")
	List<User> getAllUsers(){
		return userRepository.findAll(); 
	}
	@GetMapping("/user/{id}")
	User getUserById(@PathVariable("id") Long id) {
	    Optional<User> optionalUser = userRepository.findById(id);
	    return optionalUser.orElseThrow(()->new NotFoundUserIdExceotion(id)); 
	}
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("userId") long userId) {
	    Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
	    List<Order> orders = orderRespository.findByUser(user);

	    // Tính tổng tiền của đơn hàng cho người dùng
	    for (Order order : orders) {
	        double totalOrderAmount = 0.0;
	        List<DetailOrder> detailOrders = order.getDetailOrders();
	        for (DetailOrder detailOrder : detailOrders) {
	            Product product = detailOrder.getProduct();
	            int quantity = detailOrder.getQuantity();
	            double price = product.getPrice(); // Giá của sản phẩm
	            totalOrderAmount += price * quantity;
	        }
	        order.setTotal(totalOrderAmount); // Đặt tổng tiền vào đơn hàng
	    }

	    return ResponseEntity.ok(orders);
	}

//	@PutMapping("/user/{id}")
//	User updateUser(@RequestBody User newUser, @PathVariable("id") Long id) {
//	    Optional<User> optionalUser = userRepository.findById(id);
//	    if (optionalUser.isPresent()) {
//	        User user = optionalUser.get();
////	        user.setName(newUser.getName());
//	        user.setUsername(newUser.getUsername());
//	        user.setEmail(newUser.getEmail());
//	        return userRepository.save(user);
//	    } else {
//	        throw new UserNotFoundException(id);
//	    }
//	}
//
//	
//	
//	@DeleteMapping("/user/{id}")
//	String deleteUser(@PathVariable("id") Long id) {
//		if(!userRepository.existsById(id)) {
//			throw new UserNotFoundException(id);
//		}
//		userRepository.deleteById(id);
//		return "User with id" +id+ "has been deleted success";
//	}
	

}
