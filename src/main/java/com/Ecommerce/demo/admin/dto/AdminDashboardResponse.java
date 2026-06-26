package com.Ecommerce.demo.admin.dto;

import java.math.BigDecimal;

public class AdminDashboardResponse {

    private Long totalUsers;

    private Long totalProducts;

    private Long totalOrders;

    private BigDecimal totalRevenue;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            Long totalUsers,
            Long totalProducts,
            Long totalOrders,
            BigDecimal totalRevenue
    ) {
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
}