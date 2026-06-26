package com.Ecommerce.demo.cart.service;

import com.Ecommerce.demo.cart.dto.AddToCartRequest;
import com.Ecommerce.demo.cart.dto.CartItemResponse;
import com.Ecommerce.demo.cart.dto.CartResponse;
import com.Ecommerce.demo.cart.entity.CartItem;
import com.Ecommerce.demo.cart.repository.CartItemRepository;
import com.Ecommerce.demo.product.entity.Product;

import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartResponse addToCart(
            AddToCartRequest request,
            String email
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        // Only require size if the product actually has sizes defined
        boolean productHasSizes = product.getAvailableSizes() != null
                && !product.getAvailableSizes().isBlank();

        if (productHasSizes &&
                (request.getSelectedSize() == null ||
                        request.getSelectedSize().isBlank())) {
            throw new RuntimeException("Please select a size");
        }
        if (product.getStockQuantity() <= 0) {
            throw new RuntimeException(
                    "Product is out of stock"
            );
        }


        if (request.getQuantity() <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        if (request.getQuantity() >
                product.getStockQuantity()) {

            throw new RuntimeException(
                    "Requested quantity exceeds available stock"
            );
        }

        // Replace the existingItem lookup:
        CartItem existingItem;
        if (productHasSizes && request.getSelectedSize() != null) {
            existingItem = cartItemRepository
                    .findByUserIdAndProductIdAndSelectedSize(
                            user.getId(),
                            product.getId(),
                            request.getSelectedSize()
                    );
        } else {
            // For products without sizes, find by user + product only
            existingItem = cartItemRepository
                    .findByUserIdAndProductId(
                            user.getId(),
                            product.getId()
                    );
        }
        if (existingItem != null) {

            int newQuantity =
                    existingItem.getQuantity()
                            + request.getQuantity();

            if (newQuantity >
                    product.getStockQuantity()) {

                throw new RuntimeException(
                        "Requested quantity exceeds available stock"
                );
            }

            existingItem.setQuantity(newQuantity);

            cartItemRepository.save(existingItem);

        } else {

            CartItem cartItem = new CartItem();

            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setSelectedSize(request.getSelectedSize());

            cartItemRepository.save(cartItem);
        }

        return getCart(email);
    }

    @Override
    public CartResponse getCart(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByUserId(
                        user.getId()
                );

        List<CartItemResponse> responses =
                new ArrayList<>();

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            Product product =
                    item.getProduct();

            CartItemResponse response =
                    new CartItemResponse(
                            item.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity(),
                            item.getSelectedSize(),
                            product.getImageUrl()
                    );

            response.setSelectedSize(
                    item.getSelectedSize()
            );

            responses.add(response);
            totalAmount =
                    totalAmount.add(
                            product.getPrice()
                                    .multiply(
                                            BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    )
                    );
        }

        return new CartResponse(
                responses,
                totalAmount
        );
    }
    @Override
    public CartResponse updateCartItem(
            Long cartItemId,
            Integer quantity,
            String selectedSize,
            String email
    ) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                ));

        Product product =
                cartItem.getProduct();

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        if (quantity >
                product.getStockQuantity()) {
            throw new RuntimeException(
                    "Requested quantity exceeds available stock"
            );
        }

        boolean productHasSizes = product.getAvailableSizes() != null
                && !product.getAvailableSizes().isBlank();

        if (productHasSizes &&
                (selectedSize == null || selectedSize.isBlank())) {
            throw new RuntimeException("Please select a size");
        }


        CartItem existingItem;
        if (productHasSizes && selectedSize != null) {
            existingItem = cartItemRepository
                    .findByUserIdAndProductIdAndSelectedSize(
                            cartItem.getUser().getId(),
                            product.getId(),
                            selectedSize
                    );
        } else {
            existingItem = cartItemRepository
                    .findByUserIdAndProductId(
                            cartItem.getUser().getId(),
                            product.getId()
                    );
        }

        if (existingItem != null &&
                !existingItem.getId()
                        .equals(cartItem.getId())) {

            int newQuantity =
                    existingItem.getQuantity()
                            + quantity;

            if (newQuantity >
                    product.getStockQuantity()) {
                throw new RuntimeException(
                        "Requested quantity exceeds available stock"
                );
            }

            existingItem.setQuantity(newQuantity);

            cartItemRepository.save(existingItem);

            cartItemRepository.delete(cartItem);

        } else {

            cartItem.setQuantity(quantity);
            cartItem.setSelectedSize(selectedSize);

            cartItemRepository.save(cartItem);
        }

        return getCart(email);
    }
    @Override
    public void removeCartItem(
            Long cartItemId,
            String email
    ) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                ));

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    @Override
    public void clearCart(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        cartItemRepository.deleteAllByUserId(
                user.getId()
        );
    }
}