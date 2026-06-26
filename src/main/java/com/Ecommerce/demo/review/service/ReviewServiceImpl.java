package com.Ecommerce.demo.review.service;

import com.Ecommerce.demo.product.entity.Product;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.review.dto.ReviewRequest;
import com.Ecommerce.demo.review.dto.ReviewResponse;
import com.Ecommerce.demo.review.entity.Review;
import com.Ecommerce.demo.review.repository.ReviewRepository;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ReviewResponse addReview(
            ReviewRequest request,
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

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        return new ReviewResponse(
                review.getId(),
                user.getFullName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    @Override
    public List<ReviewResponse> getProductReviews(
            Long productId
    ) {

        return reviewRepository
                .findByProductId(productId)
                .stream()
                .map(review ->
                        new ReviewResponse(
                                review.getId(),
                                review.getUser().getFullName(),
                                review.getRating(),
                                review.getComment(),
                                review.getCreatedAt()
                        )
                )
                .toList();
    }

    @Override
    public void deleteReview(
            Long reviewId,
            String email
    ) {

        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        reviewRepository.delete(review);
    }
}