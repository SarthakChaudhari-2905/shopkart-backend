package com.Ecommerce.demo.wishlist.repository;

import com.Ecommerce.demo.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository
        extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByUserId(
            Long userId
    );

    WishlistItem findByUserIdAndProductId(
            Long userId,
            Long productId
    );
}