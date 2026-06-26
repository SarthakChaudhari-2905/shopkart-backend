package com.Ecommerce.demo.product.service;

import com.Ecommerce.demo.product.dto.ProductRequest;
import com.Ecommerce.demo.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    Page<ProductResponse> getAllProducts(String keyword, String category, Pageable pageable);
    ProductResponse getProductById(Long id);
    ProductResponse updateProduct(Long id , ProductRequest request );
    void deleteProduct(Long id);

}