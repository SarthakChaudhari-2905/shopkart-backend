package com.Ecommerce.demo.product.controller;

import com.Ecommerce.demo.product.dto.ProductRequest;
import com.Ecommerce.demo.product.dto.ProductResponse;
import com.Ecommerce.demo.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;
import com.Ecommerce.demo.s3.service.FileUploadService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;


    private final FileUploadService fileUploadService;

    public ProductController(
            ProductService productService,
            FileUploadService fileUploadService
    ) {
        this.productService = productService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    public ProductResponse createProduct(
            @Valid
            @RequestBody
            ProductRequest request
    ) {

        return productService
                .createProduct(request);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        return productService.getAllProducts(keyword, category, pageable);
    }
    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable Long id
    ) {

        return productService.getProductById(id);
    }
    @PutMapping("/{id}")
    public ProductResponse updateProduct( @PathVariable Long id ,@RequestBody ProductRequest request){
        return productService.updateProduct(
                id , request
        );
    }
    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return "Product deleted successfully";
    }
    @PostMapping("/with-image")
    public ProductResponse createProductWithImage(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Integer stockQuantity,
            @RequestParam String sku,
            @RequestParam String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String availableSizes, // ← add this
            @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = fileUploadService.uploadFile(image);
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setDescription(description);
        request.setPrice(price);
        request.setStockQuantity(stockQuantity);
        request.setSku(sku);
        request.setBrand(brand);
        request.setCategory(category);
        request.setAvailableSizes(availableSizes); // ← add this
        request.setImageUrl(imageUrl);
        return productService.createProduct(request);
    }
}