package com.Ecommerce.demo.order.repository;

import com.Ecommerce.demo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
    long count();
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount),0)
        FROM Order o
        """)
    BigDecimal getTotalRevenue();
    List<Order> findAll();
}