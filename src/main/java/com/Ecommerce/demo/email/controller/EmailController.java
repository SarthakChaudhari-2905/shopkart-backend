package com.Ecommerce.demo.email.controller;

import com.Ecommerce.demo.email.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String testEmail() {

        emailService.sendEmail(
                "chaudharisarthak295@gmail.com",
                "Test Email",
                "Spring Boot Email Working"
        );

        return "Email Sent";
    }
}