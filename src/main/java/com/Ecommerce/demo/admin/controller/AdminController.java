package com.Ecommerce.demo.admin.controller;

import com.Ecommerce.demo.admin.dto.AdminDashboardResponse;
import com.Ecommerce.demo.admin.dto.UpdateOrderStatusRequest;
import com.Ecommerce.demo.admin.service.AdminService;
import com.Ecommerce.demo.order.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(
            AdminService adminService
    ) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {

        return adminService.getDashboard();
    }
    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders() {

        return adminService.getAllOrders();
    }
    @PutMapping("/orders/{orderId}/status")
    public OrderResponse updateOrderStatus(

            @PathVariable
            Long orderId,

            @RequestBody
            UpdateOrderStatusRequest request
    ) {

        return adminService.updateOrderStatus(
                orderId,
                request.getStatus().name()
        );
    }
}