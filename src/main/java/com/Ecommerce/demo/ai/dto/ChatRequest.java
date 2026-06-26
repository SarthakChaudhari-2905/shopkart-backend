package com.Ecommerce.demo.ai.dto;

public class ChatRequest {
    private String message;
    private String conversationHistory;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getConversationHistory() { return conversationHistory; }
    public void setConversationHistory(String h) { this.conversationHistory = h; }
}