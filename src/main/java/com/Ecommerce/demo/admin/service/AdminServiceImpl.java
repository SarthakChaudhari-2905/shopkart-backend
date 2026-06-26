package com.Ecommerce.demo.admin.service;

import com.Ecommerce.demo.admin.dto.AdminDashboardResponse;
import com.Ecommerce.demo.order.repository.OrderRepository;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.Ecommerce.demo.order.dto.OrderItemResponse;
import com.Ecommerce.demo.order.dto.OrderResponse;
import com.Ecommerce.demo.order.entity.Order;
import com.Ecommerce.demo.order.entity.OrderItem;
import com.Ecommerce.demo.order.entity.OrderStatus;
import com.Ecommerce.demo.order.repository.OrderItemRepository;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;

@Service
public class AdminServiceImpl
        implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;
    public AdminServiceImpl(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository=orderItemRepository;
    }

    @Override
    public AdminDashboardResponse getDashboard() {

        Long totalUsers =
                userRepository.count();

        Long totalProducts =
                productRepository.count();

        Long totalOrders =
                orderRepository.count();

        BigDecimal totalRevenue =
                orderRepository.getTotalRevenue();

        return new AdminDashboardResponse(
                totalUsers,
                totalProducts,
                totalOrders,
                totalRevenue
        );
    }
    @Override
    public List<OrderResponse> getAllOrders() {

        List<OrderResponse> responses =
                new ArrayList<>();

        for (Order order : orderRepository.findAll()) {

            responses.add(
                    mapToResponse(order)
            );
        }

        return responses;
    }
    @Override
    public OrderResponse updateOrderStatus(
            Long orderId,
            String status
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        order.setStatus(
                OrderStatus.valueOf(status)
        );

        orderRepository.save(order);

        return mapToResponse(order);
    }
    private OrderResponse mapToResponse(Order order) {

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> responses = new ArrayList<>();

        for (OrderItem item : items) {
            // Guard against deleted products (product_id was set to NULL)
            Long productId     = item.getProduct() != null ? item.getProduct().getId()   : null;
            String productName = item.getProduct() != null ? item.getProduct().getName() : "Deleted Product";

            responses.add(
                    new OrderItemResponse(
                            productId,
                            productName,
                            item.getQuantity(),
                            item.getPrice()
                    )
            );
        }

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                responses
        );
    }
}