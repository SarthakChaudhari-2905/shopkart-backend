package com.Ecommerce.demo.productimage.controller;

import com.Ecommerce.demo.productimage.service.ProductImageService;
import com.Ecommerce.demo.s3.service.FileUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;
    private final FileUploadService   fileUploadService;

    public ProductImageController(
            ProductImageService productImageService,
            FileUploadService   fileUploadService
    ) {
        this.productImageService = productImageService;
        this.fileUploadService   = fileUploadService;
    }

    // Add image by URL
    @PostMapping("/{productId}")
    public String addImage(
            @PathVariable Long productId,
            @RequestParam String imageUrl
    ) {
        return productImageService.uploadProductImage(productId, imageUrl);
    }

    // Upload image file → S3 → save URL
    @PostMapping("/upload/{productId}")
    public String uploadImageFile(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) {
        String imageUrl = fileUploadService.uploadFile(image);
        return productImageService.uploadProductImage(productId, imageUrl);
    }
}