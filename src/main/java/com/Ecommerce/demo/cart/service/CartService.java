package com.Ecommerce.demo.cart.service;

import com.Ecommerce.demo.cart.dto.AddToCartRequest;
import com.Ecommerce.demo.cart.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(
            AddToCartRequest request,
            String email
    );

    CartResponse getCart(String email);
    CartResponse updateCartItem(
            Long cartItemId,
            Integer quantity,
            String selectedSize,
            String email
    );

    void removeCartItem(
            Long cartItemId,
            String email
    );

    void clearCart(
            String email
    );
}