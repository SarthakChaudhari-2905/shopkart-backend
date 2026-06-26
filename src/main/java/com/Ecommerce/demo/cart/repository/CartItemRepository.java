package com.Ecommerce.demo.cart.repository;

import com.Ecommerce.demo.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);

    CartItem findByUserIdAndProductIdAndSelectedSize(
            Long userId,
            Long productId,
            String selectedSize
    );

    CartItem findByUserIdAndProductId(Long userId, Long productId);
    @Transactional
    void deleteAllByUserId(Long userId);
}