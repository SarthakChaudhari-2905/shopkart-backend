package com.Ecommerce.demo.kafka.dto;

import java.math.BigDecimal;

public class OrderEvent {

    private Long orderId;

    private String customerEmail;

    private BigDecimal totalAmount;

    public OrderEvent() {
    }

    public OrderEvent(
            Long orderId,
            String customerEmail,
            BigDecimal totalAmount
    ) {
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}