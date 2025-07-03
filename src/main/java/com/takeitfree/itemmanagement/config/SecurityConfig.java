package com.takeitfree.itemmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    //load the value of the key from application.properties
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/publisher-items").authenticated()
                                .requestMatchers("/my-items").authenticated()
                                .requestMatchers("/item/update").authenticated()
                                .requestMatchers("/item/marked-taken/").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2->oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        ))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3001",
                "https://take-it-free-item-management-fronte.vercel.app"
        )); // Allows frontend on port 3000
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        config.setAllowedHeaders(List.of("*")); // Allows any headers
        config.setAllowCredentials(true); // Allow cookies or Authorization headers

        // Apply this configuration to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(keyBytes, "HmacSHA256")).build();
    }
}
