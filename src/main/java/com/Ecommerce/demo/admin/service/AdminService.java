package com.Ecommerce.demo.admin.service;

import com.Ecommerce.demo.admin.dto.AdminDashboardResponse;
import com.Ecommerce.demo.order.dto.OrderResponse;

import java.util.List;

public interface AdminService {

    AdminDashboardResponse getDashboard();

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(
            Long orderId,
            String status
    );
}