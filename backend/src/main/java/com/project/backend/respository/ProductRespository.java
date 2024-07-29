package com.project.backend.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.backend.model.Product;

public interface ProductRespository extends JpaRepository<Product, Long>{
    // Thêm hàm truy vấn để lấy tất cả sản phẩm cùng với hình ảnh của chúng
    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.pictures")
    List<Product> findAllProductsWithPictures();
    
    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.pictures WHERE p.category = :category")
    List<Product> findAllProductsByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT p From Product p JOIN FETCH p.pictures WHERE p.productId = :productId")
    Product findByProductId(@Param("productId") Long productId);
}

//LEFT JOIN FETCH p.pictures