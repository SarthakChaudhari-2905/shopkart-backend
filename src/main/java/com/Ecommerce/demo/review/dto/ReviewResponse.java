package com.Ecommerce.demo.review.dto;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;

    private String userName;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(
            Long id,
            String userName,
            Integer rating,
            String comment,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}