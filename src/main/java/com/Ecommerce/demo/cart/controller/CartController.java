package com.Ecommerce.demo.cart.controller;

import com.Ecommerce.demo.cart.dto.AddToCartRequest;
import com.Ecommerce.demo.cart.dto.CartResponse;
import com.Ecommerce.demo.cart.dto.UpdateCartRequest;
import com.Ecommerce.demo.cart.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService
    ) {
        this.cartService = cartService;
    }

    @PostMapping
    public CartResponse addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication
    ) {

        return cartService.addToCart(
                request,
                authentication.getName()
        );

    }

    @GetMapping
    public CartResponse getCart(
            Authentication authentication
    ) {

        return cartService.getCart(
                authentication.getName()
        );
    }
    @PutMapping("/{cartItemId}")
    public CartResponse updateCartItem(

            @PathVariable Long cartItemId,

            @RequestBody UpdateCartRequest request,

            Authentication authentication
    ) {

        return cartService.updateCartItem(
                cartItemId,
                request.getQuantity(),
                request.getSelectedSize(),
                authentication.getName()
        );
    }
    @DeleteMapping("/{cartItemId}")
    public String removeCartItem(

            @PathVariable Long cartItemId,

            Authentication authentication
    ) {

        cartService.removeCartItem(
                cartItemId,
                authentication.getName()
        );

        return "Item removed successfully";
    }
    @DeleteMapping("/clear")
    public String clearCart(
            Authentication authentication
    ) {

        cartService.clearCart(
                authentication.getName()
        );

        return "Cart cleared successfully";
    }
}