package com.Ecommerce.demo.wishlist.controller;

import com.Ecommerce.demo.wishlist.dto.WishlistResponse;
import com.Ecommerce.demo.wishlist.service.WishlistService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService
    ) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{productId}")
    public String addToWishlist(
            @PathVariable Long productId,
            Authentication authentication
    ) {

        wishlistService.addToWishlist(
                productId,
                authentication.getName()
        );

        return "Product added to wishlist";
    }

    @GetMapping
    public List<WishlistResponse> getWishlist(
            Authentication authentication
    ) {

        return wishlistService.getWishlist(
                authentication.getName()
        );
    }

    @DeleteMapping("/{wishlistItemId}")
    public String removeFromWishlist(
            @PathVariable Long wishlistItemId,
            Authentication authentication
    ) {

        wishlistService.removeFromWishlist(
                wishlistItemId,
                authentication.getName()
        );

        return "Wishlist item removed";
    }
}