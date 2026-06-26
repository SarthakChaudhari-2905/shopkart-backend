package com.Ecommerce.demo.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ProductResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private String sku;

    private String brand;

    private String imageUrl;
    private String category;
    private List<String> images;

    private String availableSizes;

    public ProductResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

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
    public String getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String imgUrl){
        this.imageUrl=imgUrl;
    }
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Add this field
    public String getAvailableSizes() { return availableSizes; }
    public void setAvailableSizes(String s) { this.availableSizes = s; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
}