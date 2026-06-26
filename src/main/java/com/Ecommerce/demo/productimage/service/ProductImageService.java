package com.Ecommerce.demo.productimage.service;

public interface ProductImageService {

    String uploadProductImage(
            Long productId,
            String imageUrl
    );
}