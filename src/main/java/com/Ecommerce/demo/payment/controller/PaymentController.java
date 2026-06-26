package com.Ecommerce.demo.payment.controller;

import com.Ecommerce.demo.order.service.OrderService;
import com.Ecommerce.demo.payment.dto.PaymentOrderRequest;
import com.Ecommerce.demo.payment.dto.PaymentVerifyRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Step 1 — Create Razorpay order (called when user clicks "Place Order")
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(
            @RequestBody PaymentOrderRequest request
    ) throws RazorpayException {

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount",   request.getAmount()); // amount in paise
        options.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
        options.put("receipt",  "receipt_" + System.currentTimeMillis());

        Order razorpayOrder = client.orders.create(options);

        return Map.of(
                "orderId",  razorpayOrder.get("id"),
                "amount",   razorpayOrder.get("amount"),
                "currency", razorpayOrder.get("currency"),
                "keyId",    keyId
        );
    }

    // Step 2 — Verify payment + place actual order in our DB
    @PostMapping("/verify")
    public Map<String, Object> verifyAndPlaceOrder(
            @RequestBody PaymentVerifyRequest request,
            Authentication authentication
    ) throws RazorpayException {

        // Verify Razorpay signature (prevents fraud)
        String payload = request.getRazorpayOrderId()
                + "|" + request.getRazorpayPaymentId();

        boolean isValid = Utils.verifyPaymentSignature(
                new JSONObject(Map.of(
                        "razorpay_order_id",   request.getRazorpayOrderId(),
                        "razorpay_payment_id", request.getRazorpayPaymentId(),
                        "razorpay_signature",  request.getRazorpaySignature()
                )),
                keySecret
        );

        if (!isValid) {
            throw new RuntimeException("Payment verification failed");
        }

        // Signature valid — now place the real order
        var orderResponse = orderService.placeOrder(authentication.getName());

        return Map.of(
                "success",         true,
                "orderId",         orderResponse.getOrderId(),
                "razorpayOrderId", request.getRazorpayOrderId(),
                "paymentId",       request.getRazorpayPaymentId()
        );
    }
}