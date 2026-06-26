package com.Ecommerce.demo.order.service;

import com.Ecommerce.demo.cart.entity.CartItem;
import com.Ecommerce.demo.cart.repository.CartItemRepository;
import com.Ecommerce.demo.order.dto.OrderItemResponse;
import com.Ecommerce.demo.order.dto.OrderResponse;
import com.Ecommerce.demo.order.entity.Order;
import com.Ecommerce.demo.order.entity.OrderItem;
import com.Ecommerce.demo.order.entity.OrderStatus;
import com.Ecommerce.demo.product.entity.ProductStatus;
import com.Ecommerce.demo.order.repository.OrderItemRepository;
import com.Ecommerce.demo.order.repository.OrderRepository;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Ecommerce.demo.kafka.dto.OrderEvent;
import com.Ecommerce.demo.kafka.producer.OrderProducer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    private final OrderProducer orderProducer;
    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderProducer orderProducer
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderProducer = orderProducer;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByUserId(
                        user.getId()
                );

        if (cartItems.isEmpty()) {
            throw new RuntimeException(
                    "Cart is empty"
            );
        }

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            totalAmount =
                    totalAmount.add(
                            item.getProduct()
                                    .getPrice()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    )
                    );
        }

        Order order = new Order();

        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        List<OrderItemResponse> itemResponses =
                new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(
                    cartItem.getProduct()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setPrice(
                    cartItem.getProduct()
                            .getPrice()
            );

            if (cartItem.getProduct().getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for "
                                + cartItem.getProduct().getName()
                );
            }

            cartItem.getProduct().setStockQuantity(
                    cartItem.getProduct().getStockQuantity()
                            - cartItem.getQuantity()
            );

            if (cartItem.getProduct().getStockQuantity() == 0) {

                cartItem.getProduct().setStatus(
                        ProductStatus.OUT_OF_STOCK
                );
            }
            productRepository.save(
                    cartItem.getProduct()
            );
            orderItemRepository.save(orderItem);



            itemResponses.add(
                    new OrderItemResponse(
                            cartItem.getProduct().getId(),
                            cartItem.getProduct().getName(),
                            cartItem.getQuantity(),
                            cartItem.getProduct().getPrice()
                    )
            );
        }
        OrderEvent event =
                new OrderEvent(
                        order.getId(),
                        user.getEmail(),
                        totalAmount
                );

        orderProducer.sendOrderEvent(
                event
        );


        cartItemRepository.deleteAll(cartItems);

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemResponses
        );
    }
    @Override
    public List<OrderResponse> getMyOrders(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        List<Order> orders =
                orderRepository.findByUserId(
                        user.getId()
                );

        List<OrderResponse> responses =
                new ArrayList<>();

        for (Order order : orders) {

            responses.add(
                    mapToResponse(order)
            );
        }

        return responses;
    }
    @Override
    public OrderResponse getOrderById(
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                ));

        return mapToResponse(order);
    }
    private OrderResponse mapToResponse(Order order) {

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(order.getId());

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : orderItems) {

            // Guard against deleted products (product_id is NULL)
            Long productId     = item.getProduct() != null ? item.getProduct().getId()   : null;
            String productName = item.getProduct() != null ? item.getProduct().getName() : "Deleted Product";

            itemResponses.add(
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
                itemResponses
        );
    }
}