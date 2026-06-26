package com.Ecommerce.demo.ai.controller;

import com.Ecommerce.demo.ai.dto.ChatRequest;
import com.Ecommerce.demo.ai.dto.DescriptionRequest;
import com.Ecommerce.demo.ai.service.AiService;
import com.Ecommerce.demo.product.dto.ProductResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    // GET /api/ai/recommendations/{productId}
    @GetMapping("/recommendations/{productId}")
    public List<ProductResponse> getRecommendations(
            @PathVariable Long productId) {
        return aiService.getRecommendations(productId);
    }

    // POST /api/ai/search  body: { "query": "gifts under 2000" }
    @PostMapping("/search")
    public List<ProductResponse> smartSearch(
            @RequestBody Map<String, String> body) {
        return aiService.smartSearch(body.get("query"));
    }

    // POST /api/ai/generate-description
    @PostMapping("/generate-description")
    public Map<String, String> generateDescription(
            @RequestBody DescriptionRequest request) {
        String description = aiService.generateDescription(
                request.getName(), request.getBrand(), request.getCategory());
        return Map.of("description", description);
    }

    // POST /api/ai/chat
    @PostMapping("/chat")
    public Map<String, String> chat(
            @RequestBody ChatRequest request) {
        String reply = aiService.chat(
                request.getMessage(),
                request.getConversationHistory());
        return Map.of("reply", reply);
    }
}