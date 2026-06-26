package com.Ecommerce.demo.ai.service;

import com.Ecommerce.demo.product.dto.ProductResponse;
import com.Ecommerce.demo.product.entity.Product;
import com.Ecommerce.demo.product.repository.ProductRepository;
import com.Ecommerce.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AiServiceImpl implements AiService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
    private static final String MODEL = "gemini-2.5-flash";

    public AiServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.restTemplate = new RestTemplate();
    }

    // ── 1. Product Recommendations ──
    @Override
    public List<ProductResponse> getRecommendations(Long productId) {
        Product source = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Get all products except the source
        List<Product> allProducts = productRepository.findAll(PageRequest.of(0, 50))
                .getContent()
                .stream()
                .filter(p -> !p.getId().equals(productId))
                .collect(Collectors.toList());

        if (allProducts.isEmpty()) return List.of();

        // Build product catalog for AI
        String catalog = allProducts.stream()
                .map(p -> String.format("ID:%d | %s | %s | ₹%s | Category:%s",
                        p.getId(), p.getName(), p.getBrand(),
                        p.getPrice().toPlainString(),
                        p.getCategory() != null ? p.getCategory() : "General"))
                .collect(Collectors.joining("\n"));

        String prompt = String.format("""
                You are a product recommendation engine for an e-commerce store.
                
                Source product: %s by %s in category %s priced at ₹%s
                
                Available products:
                %s
                
                Select exactly 4 product IDs that are most similar or complementary to the source product.
                Consider: same category, same brand, similar price range, complementary use.
                
                Reply with ONLY the 4 product IDs separated by commas. Example: 1,5,12,23
                No other text, no explanation.
                """,
                source.getName(), source.getBrand(),
                source.getCategory(), source.getPrice().toPlainString(),
                catalog);

        String response = callGemini(prompt);

        // Parse IDs from response
        try {
            List<Long> recommendedIds = Arrays.stream(response.trim().split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            return allProducts.stream()
                    .filter(p -> recommendedIds.contains(p.getId()))
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Fallback: return first 4 products from same category
            return allProducts.stream()
                    .filter(p -> source.getCategory() != null &&
                            source.getCategory().equals(p.getCategory()))
                    .limit(4)
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
    }

    // ── 2. Smart Search ──
    @Override
    public List<ProductResponse> smartSearch(String query) {
        List<Product> allProducts = productRepository.findAll(PageRequest.of(0, 100))
                .getContent();

        String catalog = allProducts.stream()
                .map(p -> String.format("ID:%d | %s | %s | ₹%s | Category:%s | Stock:%d",
                        p.getId(), p.getName(), p.getBrand(),
                        p.getPrice().toPlainString(),
                        p.getCategory() != null ? p.getCategory() : "General",
                        p.getStockQuantity()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format("""
                You are a smart product search engine for an Indian e-commerce store.
                
                User query: "%s"
                
                Available products:
                %s
                
                Find products that best match the user's query.
                Consider: price range, category, use case, occasion, recipient.
                
                Reply with ONLY the matching product IDs separated by commas (max 8 results).
                Only include in-stock products (Stock > 0).
                If no products match, reply with: NONE
                No other text.
                """,
                query, catalog);

        String response = callGemini(prompt);

        if (response.trim().equals("NONE") || response.trim().isEmpty()) {
            return List.of();
        }

        try {
            List<Long> matchedIds = Arrays.stream(response.trim().split(","))
                    .map(String::trim)
                    .filter(s -> s.matches("\\d+"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            return allProducts.stream()
                    .filter(p -> matchedIds.contains(p.getId()))
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    // ── 3. Generate Description ──
    @Override
    public String generateDescription(String name, String brand, String category) {
        String prompt = String.format("""
                Write a compelling product description for an Indian e-commerce website.
                
                Product: %s
                Brand: %s
                Category: %s
                
                Requirements:
                - 2-3 sentences, professional and engaging
                - Highlight key features and benefits
                - Suitable for Indian market
                - No markdown, plain text only
                - End with a value proposition
                
                Write only the description, nothing else.
                """,
                name, brand, category != null ? category : "General");

        return callGemini(prompt);
    }

    // ── 4. Chat ──
    @Override
    public String chat(String message, String conversationHistory) {
        List<Product> products = productRepository.findAll(PageRequest.of(0, 50)).getContent();

        String catalog = products.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .map(p -> String.format("%s by %s - ₹%s (%s)",
                        p.getName(), p.getBrand(),
                        p.getPrice().toPlainString(),
                        p.getCategory() != null ? p.getCategory() : "General"))
                .collect(Collectors.joining("\n"));

        String systemPrompt = String.format("""
                You are SHOPKART's friendly AI shopping assistant.
                Help customers find products, answer questions, and provide recommendations.
                
                Available products in stock:
                %s
                
                Guidelines:
                - Be helpful, friendly, and concise (max 3 sentences)
                - Recommend specific products from the catalog when relevant
                - Mention prices in ₹
                - If asked about something not in catalog, suggest alternatives
                - Always end with a helpful follow-up question or suggestion
                """, catalog);

        String fullPrompt = conversationHistory != null && !conversationHistory.isEmpty()
                ? conversationHistory + "\nUser: " + message
                : message;

        return callGeminiWithSystem(systemPrompt,
                fullPrompt);
    }

    // ── Claude API call ──
    private String callGemini(String userMessage) {
        return callGeminiWithSystem(null, userMessage);
    }

    private String callGeminiWithSystem(String systemPrompt,
                                        String userMessage) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String prompt = systemPrompt != null
                ? systemPrompt + "\n\n" + userMessage
                : userMessage;

        Map<String, Object> body = new HashMap<>();

        body.put("contents", List.of(
                Map.of(
                        "parts",
                        List.of(
                                Map.of("text", prompt)
                        )
                )
        ));

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        try {

            String url =
                    GEMINI_API_URL + "?key=" + apiKey;

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(
                            url,
                            entity,
                            Map.class
                    );

            Map responseBody = response.getBody();

            List candidates =
                    (List) responseBody.get("candidates");

            Map candidate =
                    (Map) candidates.get(0);

            Map content =
                    (Map) candidate.get("content");

            List parts =
                    (List) content.get("parts");

            Map part =
                    (Map) parts.get(0);

            return (String) part.get("text");

        } catch (Exception e) {
            System.err.println("Gemini API Error: "
                    + e.getMessage());
            return "";
        }
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse r = new ProductResponse();
        r.setId(product.getId());
        r.setName(product.getName());
        r.setDescription(product.getDescription());
        r.setPrice(product.getPrice());
        r.setStockQuantity(product.getStockQuantity());
        r.setSku(product.getSku());
        r.setBrand(product.getBrand());
        r.setCategory(product.getCategory());
        r.setImageUrl(product.getImageUrl());
        if (product.getImages() != null) {
            r.setImages(product.getImages().stream()
                    .map(img -> img.getImageUrl())
                    .collect(Collectors.toList()));
        }
        return r;
    }
}