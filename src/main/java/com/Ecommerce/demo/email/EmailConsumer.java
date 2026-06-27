package com.Ecommerce.demo.email;

import com.Ecommerce.demo.kafka.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order-topic", groupId = "email-group")
    public void consume(OrderEvent event) {

        String subject = "✅ Order Confirmed — #" + event.getOrderId() + " | SHOPKART";
        String body    = buildOrderConfirmationEmail(event);

        emailService.sendEmail(event.getCustomerEmail(), subject, body);

        System.out.println("Order confirmation email sent for order #" + event.getOrderId());
    }

    private String buildOrderConfirmationEmail(OrderEvent event) {
        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        return """
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                           SHOPKART
                     Your Order is Confirmed!
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                
                Dear Customer,
                
                Thank you for shopping with SHOPKART! We're thrilled
                to let you know that your order has been successfully
                placed and is now being processed.
                
                ┌─────────────────────────────────────────┐
                │           ORDER SUMMARY                  │
                ├─────────────────────────────────────────┤
                │  Order ID     : #""" + event.getOrderId() + """
                
                │  Order Date   : """ + date + """
                
                │  Total Amount : ₹""" + event.getTotalAmount() + """
                
                │  Status       : CONFIRMED ✓
                └─────────────────────────────────────────┘
                
                WHAT HAPPENS NEXT?
                
                  1. ✅  Order Confirmed  (Done!)
                  2. 📦  Processing & Packing  (In progress)
                  3. 🚚  Out for Delivery  (Coming soon)
                  4. 🏠  Delivered to your doorstep
                
                ─────────────────────────────────────────
                
                You can track your order anytime at:
                👉  http://localhost:5173/orders/""" + event.getOrderId() + """
                
                
                NEED HELP?
                Our support team is available 24/7.
                Email  : support@shopkart.in
                Phone  : 1800-XXX-XXXX (Toll Free)
                
                ─────────────────────────────────────────
                
                💡 Pro Tip: Download the SHOPKART app for
                   real-time order tracking and exclusive
                   app-only deals!
                
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                Thank you for choosing SHOPKART!
                We hope you love your purchase. 🛍️
                
                © 2026 SHOPKART. All rights reserved.
                This is an automated email. Please do not reply.
                ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                """;
    }
}
