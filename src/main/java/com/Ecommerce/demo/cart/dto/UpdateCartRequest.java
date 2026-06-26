package com.Ecommerce.demo.cart.dto;

public class UpdateCartRequest {

    private Integer quantity;
    private String selectedSize;

    public UpdateCartRequest() {
    }

    public UpdateCartRequest(Integer quantity) {
        this.quantity = quantity;
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