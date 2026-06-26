package com.Ecommerce.demo.product.service;

import com.Ecommerce.demo.product.dto.ProductRequest;
import com.Ecommerce.demo.product.dto.ProductResponse;
import com.Ecommerce.demo.product.entity.Product;
import com.Ecommerce.demo.product.entity.ProductStatus;
import com.Ecommerce.demo.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setSku(request.getSku());
        product.setBrand(request.getBrand());
        product.setStatus(ProductStatus.ACTIVE);
        product.setCategory(request.getCategory());
        product.setAvailableSizes(request.getAvailableSizes());
        return mapToResponse(productRepository.save(product));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, String category, Pageable pageable) {
        if ((keyword == null || keyword.isEmpty()) && (category == null || category.isEmpty())) {
            return productRepository.findAll(pageable).map(this::mapToResponse);
        }
        if (category != null && !category.isEmpty() && (keyword == null || keyword.isEmpty())) {
            return productRepository.findByCategoryIgnoreCase(category, pageable).map(this::mapToResponse);
        }
        if (keyword != null && !keyword.isEmpty() && (category == null || category.isEmpty())) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable).map(this::mapToResponse);
        }
        return productRepository.findByNameContainingIgnoreCaseAndCategoryIgnoreCase(keyword, category, pageable).map(this::mapToResponse);
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());
        product.setBrand(request.getBrand());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setAvailableSizes(request.getAvailableSizes());
        return mapToResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public void deleteProduct(Long id) {
        // Verify product exists
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Delete all related data via native queries to bypass FK constraints
        entityManager.createNativeQuery(
                        "DELETE FROM product_images WHERE product_id = :id")
                .setParameter("id", id).executeUpdate();

        entityManager.createNativeQuery(
                        "DELETE FROM reviews WHERE product_id = :id")
                .setParameter("id", id).executeUpdate();

        entityManager.createNativeQuery(
                        "DELETE FROM wishlist_items WHERE product_id = :id")
                .setParameter("id", id).executeUpdate();

        entityManager.createNativeQuery(
                        "DELETE FROM cart_items WHERE product_id = :id")
                .setParameter("id", id).executeUpdate();

        // Note: we do NOT delete order_items — orders are financial records
        // Just nullify the product reference instead
        entityManager.createNativeQuery(
                        "UPDATE order_items SET product_id = NULL WHERE product_id = :id")
                .setParameter("id", id).executeUpdate();

        entityManager.createNativeQuery(
                        "DELETE FROM products WHERE id = :id")
                .setParameter("id", id).executeUpdate();
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setSku(product.getSku());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setAvailableSizes(product.getAvailableSizes());

        if (product.getImages() != null) {
            response.setImages(
                    product.getImages().stream()
                            .map(img -> img.getImageUrl())
                            .collect(java.util.stream.Collectors.toList())
            );
        }
        return response;
    }
}