package com.Ecommerce.demo.email;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );
}