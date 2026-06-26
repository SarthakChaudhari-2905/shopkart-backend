package com.Ecommerce.demo.ai.service;

import com.Ecommerce.demo.product.dto.ProductResponse;
import java.util.List;

public interface AiService {
    List<ProductResponse> getRecommendations(Long productId);
    List<ProductResponse> smartSearch(String query);
    String generateDescription(String name, String brand, String category);
    String chat(String message, String conversationHistory);
}