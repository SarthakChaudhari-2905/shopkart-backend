package com.Ecommerce.demo.review.controller;

import com.Ecommerce.demo.review.dto.ReviewRequest;
import com.Ecommerce.demo.review.dto.ReviewResponse;
import com.Ecommerce.demo.review.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(
            ReviewService reviewService
    ) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse addReview(
            @RequestBody ReviewRequest request,
            Authentication authentication
    ) {

        return reviewService.addReview(
                request,
                authentication.getName()
        );
    }

    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getProductReviews(
            @PathVariable Long productId
    ) {

        return reviewService.getProductReviews(
                productId
        );
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {

        reviewService.deleteReview(
                reviewId,
                authentication.getName()
        );

        return "Review deleted successfully";
    }
}