package com.Ecommerce.demo.admin.dto;

import com.Ecommerce.demo.order.entity.OrderStatus;

public class UpdateOrderStatusRequest {

    private OrderStatus status;

    public UpdateOrderStatusRequest() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}