package com.Ecommerce.demo.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public String profile(
            Authentication authentication) {

        return "Logged in as : "
                + authentication.getName();
    }
}