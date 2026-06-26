package com.Ecommerce.demo.productimage.repository;

import com.Ecommerce.demo.productimage.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductId(
            Long productId
    );
}