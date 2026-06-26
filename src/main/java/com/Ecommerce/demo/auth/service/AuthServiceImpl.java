package com.Ecommerce.demo.auth.service;

import com.Ecommerce.demo.auth.dto.AuthResponse;
import com.Ecommerce.demo.auth.dto.LoginRequest;
import com.Ecommerce.demo.auth.dto.RegisterRequest;
import com.Ecommerce.demo.security.JwtService;
import com.Ecommerce.demo.user.entity.Role;
import com.Ecommerce.demo.user.entity.User;
import com.Ecommerce.demo.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Ecommerce.demo.email.EmailService;
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);

        emailService.sendEmail(
                savedUser.getEmail(),
                "Welcome to Ecommerce",
                "Hello " + savedUser.getFullName() +
                        ",\n\nWelcome to our Ecommerce Platform.\n" +
                        "Your account has been created successfully.\n\n" +
                        "Happy Shopping!"
        );

        return new AuthResponse(
                "User registered successfully"
        );
    }
    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        System.out.println("Email = " + user.getEmail());
        System.out.println("Role = " + user.getRole());
        System.out.println("DB Password = " + user.getPassword());
        System.out.println("Request Password = " + request.getPassword());

        boolean matches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        System.out.println("Password Match = " + matches);


        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name()
                );

        return new AuthResponse(token);
    }
}