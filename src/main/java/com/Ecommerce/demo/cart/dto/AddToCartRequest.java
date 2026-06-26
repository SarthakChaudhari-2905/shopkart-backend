package com.Ecommerce.demo.cart.dto;

public class AddToCartRequest {

    private Long productId;
    private Integer quantity;

    private String selectedSize;
    public AddToCartRequest() {
    }

    public AddToCartRequest(Long productId,
                            Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public String getSelectedSize() { return selectedSize; }
    public void setSelectedSize(String s) { this.selectedSize = s; }
}