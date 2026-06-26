package com.Ecommerce.demo.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stockQuantity;

    @NotBlank
    private String sku;

    @NotBlank
    private String brand;

    private String availableSizes;

    private String imageUrl;
    private String category;


    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSku() {
        return sku;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String imgUrl){
        this.imageUrl=imgUrl;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBrand() {
        return brand;
    }

    public String getAvailableSizes() { return availableSizes; }
    public void setAvailableSizes(String s) { this.availableSizes = s; }
    public void setBrand(String brand) {
        this.brand = brand;
    }
}