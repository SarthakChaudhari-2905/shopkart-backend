package com.Ecommerce.demo.wishlist.dto;

import java.math.BigDecimal;

public class WishlistResponse {

    private Long wishlistItemId;

    private Long productId;

    private String productName;

    private BigDecimal price;

    public WishlistResponse() {
    }

    public WishlistResponse(
            Long wishlistItemId,
            Long productId,
            String productName,
            BigDecimal price
    ) {
        this.wishlistItemId = wishlistItemId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public Long getWishlistItemId() {
        return wishlistItemId;
    }

    public void setWishlistItemId(Long wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}