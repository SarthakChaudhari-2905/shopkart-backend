package com.Ecommerce.demo.auth.service;

import com.Ecommerce.demo.auth.dto.AuthResponse;
import com.Ecommerce.demo.auth.dto.LoginRequest;
import com.Ecommerce.demo.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}