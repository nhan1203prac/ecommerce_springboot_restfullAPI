package com.project.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.exception.NotFoundProductIdException;
import com.project.backend.exception.NotFoundUserIdExceotion;
import com.project.backend.model.Cart;
import com.project.backend.model.CartItemDTO;
import com.project.backend.model.Picture;
import com.project.backend.model.Product;
import com.project.backend.model.User;
import com.project.backend.respository.CartRespository;
import com.project.backend.respository.ProductRespository;
import com.project.backend.respository.UserRepository;

@RestController
@CrossOrigin("http://localhost:3001")
public class CartController {
	@Autowired
	private ProductRespository productRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRespository cartRespository;
	@PostMapping("/products/add-to-cart")
	public ResponseEntity<?> AddToCart(@RequestBody Map<String, Long> requestBody) {
	    Long userId = requestBody.get("userId");
	    Long productId = requestBody.get("productId");

	    
	    Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
	    
	    
	    Optional<Product> optionalProduct = productRepository.findById(productId);
	    Product product = optionalProduct.orElseThrow(() -> new NotFoundProductIdException(productId));
	    
	    
	    Cart existingCartItem = cartRespository.findByUserUserIdAndProductProductId(userId, productId);

	    if (existingCartItem != null) {
	        existingCartItem.setQuantityOfEachProduct(existingCartItem.getQuantityOfEachProduct() + 1);
	        cartRespository.save(existingCartItem);
	    } else {
	        Cart cartItem = new Cart();
	        cartItem.setUser(user);
	        cartItem.setProduct(product);
	        cartItem.setPrice(product.getPrice());
	        cartItem.setQuantityOfEachProduct(1); 
	        // Lưu mục mới vào database
	        cartRespository.save(cartItem);
	    }

	    
	    user.setCount(user.getCount() + 1);
	    userRepository.save(user);

	    return ResponseEntity.ok(user);
	}
	
	@PostMapping("/carts/decrease-cart")
	public ResponseEntity<?> DecreaseCart(@RequestBody Map<String, Long> requestBody){
	    Long userId = requestBody.get("userId");
	    Long productId = requestBody.get("productId");

	    Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));

	    Optional<Product> optionalProduct = productRepository.findById(productId);
	    Product product = optionalProduct.orElseThrow(() -> new NotFoundProductIdException(productId));

	    Cart existingCartItem = cartRespository.findByUserUserIdAndProductProductId(userId, productId);

	    if(existingCartItem.getQuantityOfEachProduct() > 0) {
	        existingCartItem.setQuantityOfEachProduct(existingCartItem.getQuantityOfEachProduct() - 1);
	        if(user.getCount() > 0) {
	            user.setCount(user.getCount() - 1);
	        }
	        
	        // Lưu lại thông tin giỏ hàng nếu số lượng vẫn còn lớn hơn 0
	        cartRespository.save(existingCartItem);
	    } else if(existingCartItem.getQuantityOfEachProduct() ==0){
	        // Xóa mục trong giỏ hàng nếu số lượng giảm xuống dưới hoặc bằng 0
	        cartRespository.deleteById(existingCartItem.getCartId());
	    }

	    // Lưu lại thông tin người dùng sau khi xử lý giảm số lượng mục trong giỏ hàng
	    userRepository.save(user);

	    return ResponseEntity.ok(user);
	}

	
	@PostMapping("/carts/increase-cart")
	public ResponseEntity<?> IncreaseCart(@RequestBody Map<String, Long> requestBody){
		Long userId = requestBody.get("userId");
	    Long productId = requestBody.get("productId");

	    
	    Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
	    
	    
	    Optional<Product> optionalProduct = productRepository.findById(productId);
	    Product product = optionalProduct.orElseThrow(() -> new NotFoundProductIdException(productId));
	    
	    Cart existingCartItem = cartRespository.findByUserUserIdAndProductProductId(userId, productId);
	    
	   
	    existingCartItem.setQuantityOfEachProduct(existingCartItem.getQuantityOfEachProduct()+1);
	    
		user.setCount(user.getCount()+1);
		    

	    userRepository.save(user);
	    cartRespository.save(existingCartItem);
	    return ResponseEntity.ok(user);
	}
	
	@PostMapping("/carts/remove-frome-cart")
	ResponseEntity<?> Remove(@RequestBody Map<String,Long> requestBody){
		Long userId = requestBody.get("userId");
	    Long productId = requestBody.get("productId");

	    
	    Optional<User> optionalUser = userRepository.findById(userId);
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(userId));
	    
	    
	    Optional<Product> optionalProduct = productRepository.findById(productId);
	    Product product = optionalProduct.orElseThrow(() -> new NotFoundProductIdException(productId));
	    
	    Cart existingCartItem = cartRespository.findByUserUserIdAndProductProductId(userId, productId);
	    user.setCount(user.getCount() - existingCartItem.getQuantityOfEachProduct());
	    
	    cartRespository.deleteById(existingCartItem.getCartId());
	    userRepository.save(user);
	    return ResponseEntity.ok(user);
	}
	@GetMapping("/carts/{userId}")
	public ResponseEntity<List<CartItemDTO>> getCartItemsByUserId(@PathVariable("userId") Long userId) {
	   
	    List<Cart> cartItems = cartRespository.findByUserUserId(userId);

	  
	    Map<Long, Integer> productQuantityMap = new HashMap<>();
	    for (Cart cartItem : cartItems) {
	        Long productId = cartItem.getProduct().getProductId();
	        int currentQuantity = productQuantityMap.getOrDefault(productId, 0);
	        currentQuantity += cartItem.getQuantityOfEachProduct();
	        productQuantityMap.put(productId, currentQuantity);
	    }

	   
	    List<CartItemDTO> cartItemDTOs = new ArrayList<>();
	    for (Cart cartItem : cartItems) {
	        Long productId = cartItem.getProduct().getProductId();
	        if (!productQuantityMap.containsKey(productId)) {
	            continue; 
	        }

	        CartItemDTO cartItemDTO = new CartItemDTO();
	        cartItemDTO.setPrice(cartItem.getPrice());
	        cartItemDTO.setProductId(productId);
	        cartItemDTO.setProductName(cartItem.getProduct().getName());
	        
	     
	        List<String> imageUrls = cartItem.getProduct().getPictures().stream()
	                .map(Picture::getUrl)
	                .collect(Collectors.toList());
	        cartItemDTO.setImageUrls(imageUrls);
	        
	      
	        int productQuantity = productQuantityMap.get(productId);
	        cartItemDTO.setQuantity(productQuantity);
	        
	      
	        cartItemDTOs.add(cartItemDTO);
	        
	       
	        productQuantityMap.remove(productId);
	    }
	    return ResponseEntity.ok(cartItemDTOs);
	}
	

//	@GetMapping("/carts/{userId}")
//	public ResponseEntity<List<CartItemDTO>> getCartItemsByUserId(@PathVariable("userId") Long userId) {
//	    // Lấy danh sách các sản phẩm mà người dùng đã mua
//	    List<Cart> cartItems = cartRespository.findByUserUserId(userId);
//
//	    // Tính tổng số lượng của mỗi sản phẩm
////	    Map<Long, Integer> productQuantityMap = cartItems.stream()
////	            .collect(Collectors.groupingBy(cartItem -> cartItem.getProduct().getProductId(),
////	                    Collectors.summingInt(Cart::getQuantityOfEachProduct)));
//	    List<CartItemDTO> cartItemDTOs = new ArrayList<>();
//	    for (Cart cartItem : cartItems) {
//	        CartItemDTO cartItemDTO = new CartItemDTO();
//	        cartItemDTO.setProductId(cartItem.getProduct().getProductId());
//	        cartItemDTO.setProductName(cartItem.getProduct().getName());
//	        
//	        // Lấy danh sách các URL của hình ảnh của sản phẩm
//	        List<String> imageUrls = cartItem.getProduct().getPictures().stream()
//	                .map(Picture::getUrl)
//	                .collect(Collectors.toList());
//	        cartItemDTO.setImageUrls(imageUrls);
//	        
//	        // Lấy tổng số lượng của sản phẩm trong giỏ hàng
//	        int productQuantity = cartItems.stream()
//	                .filter(item -> Long.valueOf(item.getProduct().getProductId()).equals(cartItem.getProduct().getProductId()))
//	                .mapToInt(Cart::getQuantityOfEachProduct)
//	                .sum();
//
//	        cartItemDTO.setQuantity(productQuantity);
//	        
//	        cartItemDTOs.add(cartItemDTO);
//	    }
//	    return ResponseEntity.ok(cartItemDTOs);
//	}

	}
