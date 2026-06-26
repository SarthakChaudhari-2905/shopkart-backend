package com.Ecommerce.demo.wishlist.service;

import com.Ecommerce.demo.wishlist.dto.WishlistResponse;

import java.util.List;

public interface WishlistService {

    void addToWishlist(
            Long productId,
            String email
    );

    List<WishlistResponse> getWishlist(
            String email
    );

    void removeFromWishlist(
            Long wishlistItemId,
            String email
    );
}