package com.Ecommerce.demo.order.service;

import com.Ecommerce.demo.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(String email);

    List<OrderResponse> getMyOrders(String email);

    OrderResponse getOrderById(Long orderId);
}