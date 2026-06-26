package com.Ecommerce.demo.review.service;

import com.Ecommerce.demo.review.dto.ReviewRequest;
import com.Ecommerce.demo.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addReview(
            ReviewRequest request,
            String email
    );

    List<ReviewResponse> getProductReviews(
            Long productId
    );

    void deleteReview(
            Long reviewId,
            String email
    );
}