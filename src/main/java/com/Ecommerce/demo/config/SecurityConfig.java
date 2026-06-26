package com.Ecommerce.demo.config;

import com.Ecommerce.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS preflight — always allow
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth — public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Static + uploads — public
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/redis-test").permitAll()

                        // Product images — public (adding images needs auth handled on frontend)
                        .requestMatchers("/api/product-images/**").permitAll()

                        // S3 file upload — public endpoint
                        .requestMatchers("/api/files/**").permitAll()

                        // Products — GET public
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()

                        // Product with-image upload — permit (admin-only enforced on frontend)
                        .requestMatchers(HttpMethod.POST, "/api/products/with-image").permitAll()

                        // Reviews — GET public
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()

                                // Add in authorizeHttpRequests:
                                .requestMatchers(HttpMethod.GET, "/api/ai/recommendations/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/ai/search").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/ai/chat").permitAll()
                                .requestMatchers("/api/payment/**").authenticated()
// generate-description needs auth (admin only)
                        // Everything else needs JWT
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}