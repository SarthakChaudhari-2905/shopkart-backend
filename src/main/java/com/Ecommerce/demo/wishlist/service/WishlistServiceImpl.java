package com.Ecommerce.demo.wishlist.service;

import com.Ecommerce.demo.product.entity.Product;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import com.Ecommerce.demo.wishlist.dto.WishlistResponse;
import com.Ecommerce.demo.wishlist.entity.WishlistItem;
import com.Ecommerce.demo.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistServiceImpl
        implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addToWishlist(
            Long productId,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        WishlistItem existingItem =
                wishlistRepository.findByUserIdAndProductId(
                        user.getId(),
                        productId
                );

        if (existingItem != null) {
            return;
        }

        WishlistItem wishlistItem =
                new WishlistItem();

        wishlistItem.setUser(user);
        wishlistItem.setProduct(product);

        wishlistRepository.save(wishlistItem);
    }

    @Override
    public List<WishlistResponse> getWishlist(
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<WishlistItem> wishlistItems =
                wishlistRepository.findByUserId(
                        user.getId()
                );

        List<WishlistResponse> responses =
                new ArrayList<>();

        for (WishlistItem item : wishlistItems) {

            responses.add(
                    new WishlistResponse(
                            item.getId(),
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getProduct().getPrice(),
                            item.getProduct().getImageUrl()
                    )
            );
        }

        return responses;
    }

    @Override
    public void removeFromWishlist(
            Long wishlistItemId,
            String email
    ) {

        WishlistItem wishlistItem =
                wishlistRepository.findById(
                        wishlistItemId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Wishlist item not found"
                        ));

        wishlistRepository.delete(wishlistItem);
    }
}