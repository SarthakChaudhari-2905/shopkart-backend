package com.Ecommerce.demo.payment.dto;

public class PaymentVerifyRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String id) { this.razorpayOrderId = id; }
    public String getRazorpayPaymentId() { return razorpayPaymentId; }
    public void setRazorpayPaymentId(String id) { this.razorpayPaymentId = id; }
    public String getRazorpaySignature() { return razorpaySignature; }
    public void setRazorpaySignature(String sig) { this.razorpaySignature = sig; }
}