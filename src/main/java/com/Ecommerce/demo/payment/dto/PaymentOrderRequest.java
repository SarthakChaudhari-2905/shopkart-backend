package com.Ecommerce.demo.payment.dto;

public class PaymentOrderRequest {
    private Long amount; // in paise (₹1 = 100 paise)
    private String currency;

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}