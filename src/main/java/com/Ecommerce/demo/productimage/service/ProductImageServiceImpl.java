package com.Ecommerce.demo.productimage.service;

import com.Ecommerce.demo.product.entity.Product;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.productimage.entity.ProductImage;
import com.Ecommerce.demo.productimage.repository.ProductImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductImageServiceImpl
        implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ProductImageServiceImpl(
            ProductRepository productRepository,
            ProductImageRepository productImageRepository
    ) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public String uploadProductImage(
            Long productId,
            String imageUrl
    ) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        ProductImage image =
                new ProductImage();

        image.setImageUrl(imageUrl);
        image.setProduct(product);

        productImageRepository.save(image);

        return "Image added successfully";
    }
}