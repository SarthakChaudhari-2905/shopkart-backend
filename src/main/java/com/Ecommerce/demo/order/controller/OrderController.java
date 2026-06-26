package com.Ecommerce.demo.order.controller;

import com.Ecommerce.demo.order.dto.OrderResponse;
import com.Ecommerce.demo.order.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(
            Authentication authentication
    ) {

        return orderService.placeOrder(
                authentication.getName()
        );
    }

    @GetMapping
    public List<OrderResponse> getMyOrders(
            Authentication authentication
    ) {

        return orderService.getMyOrders(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(
            @PathVariable Long id
    ) {

        return orderService.getOrderById(id);
    }
}