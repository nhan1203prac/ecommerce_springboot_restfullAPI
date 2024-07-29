package com.project.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.backend.exception.NotFoundProductIdException;
import com.project.backend.exception.NotFoundUserIdExceotion;
import com.project.backend.model.CheckUserId;
import com.project.backend.model.Picture;
import com.project.backend.model.Product;
import com.project.backend.model.ProductInfoWithSimilarCategory;
import com.project.backend.model.ProductWithUrl;
import com.project.backend.model.User;
import com.project.backend.respository.PictureRespository;
import com.project.backend.respository.ProductRespository;
import com.project.backend.respository.UserRepository;
import com.stripe.model.ProductSearchResult;

@RestController
@CrossOrigin("http://localhost:3001")
public class ProductController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRespository productRepository;
	@Autowired
	private PictureRespository pictureRespository;
	@PostMapping("/products")
	public ResponseEntity<?> createProduct(@RequestBody Product productRequest) {
	    // Kiểm tra null cho productRequest
	    if (productRequest == null) {
	        return ResponseEntity.badRequest().body("Product request is null");
	    }
	    
	    // Tạo sản phẩm từ dữ liệu được gửi từ frontend
	    Product product = new Product();
	    product.setName(productRequest.getName());
	    product.setDescription(productRequest.getDescription());
	    product.setPrice(productRequest.getPrice());
	    product.setCategory(productRequest.getCategory());

	    // Lưu sản phẩm vào cơ sở dữ liệu
	    Product savedProduct = productRepository.save(product);

	 // Khởi tạo một danh sách trống cho thuộc tính pictures của savedProduct
	 savedProduct.setPictures(new ArrayList<>());

	 // Lưu thông tin về ảnh vào cơ sở dữ liệu
	 List<Picture> pictures = productRequest.getPictures();
	 // Kiểm tra null cho danh sách hình ảnh
	 if (pictures != null) {
	     for (Picture imageUrl : pictures) {
	         // Kiểm tra null cho URL của hình ảnh
	         if (imageUrl.getUrl() != null) {
	             Picture picture = new Picture();
	             picture.setProduct(savedProduct);
	             picture.setUrl(imageUrl.getUrl());
	             // Không cần lưu trữ publicId
	             // picture.setPublicId(imageUrl.getPublicId());
	             pictureRespository.save(picture);
	             savedProduct.getPictures().add(picture);
	         }
	     }
	 }

	 productRepository.save(savedProduct);
	 return ResponseEntity.ok(savedProduct);
	}
	
	@GetMapping("/products")
    public ResponseEntity<List<ProductWithUrl>> getAllProductsWithPictures() {
        List<Product> products = productRepository.findAllProductsWithPictures();
        List<ProductWithUrl> productsWithUrls = new ArrayList<>();

        for (Product product : products) {
            ProductWithUrl productWithUrl = new ProductWithUrl();
            productWithUrl.setProductId(product.getProductId());
            productWithUrl.setName(product.getName());
            productWithUrl.setDescription(product.getDescription());
            productWithUrl.setPrice(product.getPrice());
            productWithUrl.setCategory(product.getCategory());

            List<String> pictureUrls = new ArrayList<>();
            for (Picture picture : product.getPictures()) {
                pictureUrls.add(picture.getUrl());
            }
            productWithUrl.setPictureUrls(pictureUrls);

            productsWithUrls.add(productWithUrl);
        }

        return ResponseEntity.ok(productsWithUrls);
    }
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductInfoWithSimilarCategory> getProductById(@PathVariable("id") Long id) {
	    Optional<Product> optionalProduct = productRepository.findById(id);
	    Product product = optionalProduct.orElseThrow(()-> new NotFoundProductIdException(id));
	   
	    ProductWithUrl productWithUrl = new ProductWithUrl();
	    
        productWithUrl.setProductId(product.getProductId());
        productWithUrl.setName(product.getName());
        productWithUrl.setDescription(product.getDescription());
        productWithUrl.setPrice(product.getPrice());
        productWithUrl.setCategory(product.getCategory());
        
        List<String> pictureUrls = new ArrayList<>();
        for (Picture picture : product.getPictures()) {
            pictureUrls.add(picture.getUrl());
        }
        productWithUrl.setPictureUrls(pictureUrls);

        // Lấy ra các sản phẩm có cùng category với sản phẩm hiện tại
        List<Product> productsWithSameCategory = productRepository.findAllProductsByCategory(product.getCategory());
        // Tạo danh sách chứa thông tin sản phẩm cùng category
        List<ProductWithUrl> productsWithSameCategoryUrls = new ArrayList<>();
        for (Product prod : productsWithSameCategory) {
            ProductWithUrl prodWithUrl = new ProductWithUrl();
            prodWithUrl.setProductId(prod.getProductId());
            prodWithUrl.setName(prod.getName());
            prodWithUrl.setDescription(prod.getDescription());
            prodWithUrl.setPrice(prod.getPrice());
            prodWithUrl.setCategory(prod.getCategory());
            List<String> picUrls = new ArrayList<>();
            for (Picture pic : prod.getPictures()) {
                picUrls.add(pic.getUrl());
            }
            prodWithUrl.setPictureUrls(picUrls);
            productsWithSameCategoryUrls.add(prodWithUrl);
        }

        // Tạo đối tượng ProductInfoWithSimilarCategory và đặt giá trị cho các trường
        ProductInfoWithSimilarCategory productInfo = new ProductInfoWithSimilarCategory();
        productInfo.setProductWithId(productWithUrl);
        productInfo.setProductsWithSameCategory(productsWithSameCategoryUrls);

        return ResponseEntity.ok(productInfo);

	}
	@GetMapping("/products/category/{category}")
	public ResponseEntity<?> getProductWithCategory(@PathVariable("category") String category){
		 List<ProductWithUrl> productsWithUrls = new ArrayList<>();
		 List<Product>products = new ArrayList<Product>();
		if(category.equals("all")) {
			products = productRepository.findAll();
			
		}
		else {
			products = productRepository.findAllProductsByCategory(category);
		}
		

	        for (Product product : products) {
	            ProductWithUrl productWithUrl = new ProductWithUrl();
	            productWithUrl.setProductId(product.getProductId());
	            productWithUrl.setName(product.getName());
	            productWithUrl.setDescription(product.getDescription());
	            productWithUrl.setPrice(product.getPrice());
	            productWithUrl.setCategory(product.getCategory());

	            List<String> pictureUrls = new ArrayList<>();
	            for (Picture picture : product.getPictures()) {
	                pictureUrls.add(picture.getUrl());
	            }
	            productWithUrl.setPictureUrls(pictureUrls);

	            productsWithUrls.add(productWithUrl);
	        }
	        return ResponseEntity.ok(productsWithUrls);
	}
	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> removeProduct(@PathVariable("id") Long id, @RequestBody CheckUserId userId) {
	    Optional<User> optionalUser = userRepository.findById(userId.getUserId());
	    User user = optionalUser.orElseThrow(() -> new NotFoundUserIdExceotion(id));

	    if (user.isAdmin()) {
	        productRepository.deleteById(id);
	        
	        List<Product> products = productRepository.findAll();
	        return ResponseEntity.ok(products);
	    } else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this product.");
	    }
	}
	@PatchMapping("/products/{id}")
	public ResponseEntity<?> UpdateProduct(@PathVariable("id")Long id,@RequestBody ProductWithUrl productRequest){
		Optional<Product> optionalProduct = productRepository.findById(id);
		Product product = optionalProduct.orElseThrow(()-> new NotFoundUserIdExceotion(id));
		product.setName(productRequest.getName());
	    product.setDescription(productRequest.getDescription());
	    product.setPrice(productRequest.getPrice());
	    product.setCategory(productRequest.getCategory());

	    Product savedProduct = productRepository.save(product);

//	    xóa ảnh gốc
	 savedProduct.setPictures(new ArrayList<>());

	 List<String> pictures = productRequest.getPictureUrls();

	 if (pictures != null) {
	     for (String imageUrl : pictures) {
	  
	      
	             Picture picture = new Picture();
	             picture.setProduct(savedProduct);
	             picture.setUrl(imageUrl);
	 
	             Picture pt = pictureRespository.save(picture);
	             savedProduct.getPictures().add(pt);
	         
	     }
	 }

	 productRepository.save(savedProduct);
	 return ResponseEntity.ok(savedProduct);
	}

//	@GetMapping("/products")
//	public ResponseEntity<?> createProduct(){
//		Product newProduct = new Product();
//		newProduct.setName("Moust Logitech");
//		newProduct.setPrice(200);
//		newProduct.setCategory("technology");
//		newProduct.setDescription("Some description");
//		newProduct.s
//		Product savedProduct = productRepository.save(product);
//		
//		Picture picture = new Picture();
//		picture.setProduct(savedProduct);
//		pictureRespository.save(picture);
//		return ResponseEntity.ok(savedProduct);
//
//	}
}
