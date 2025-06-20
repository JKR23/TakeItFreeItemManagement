package com.takeitfree.itemmanagement.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // Marks this class as a Spring component so it can be used automatically
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    // This is Spring's default converter for JWT to authentication
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public CustomJwtAuthenticationConverter() {
        // Initialize the default converter
        this.jwtAuthenticationConverter = new JwtAuthenticationConverter();

        // Customize how authorities (roles/permissions) are extracted from the JWT
        this.jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Try to get the list of roles from the "authorities" claim in the JWT
            List<String> roles = jwt.getClaimAsStringList("authorities");

            // If no roles are found, use an empty list
            if (roles == null) {
                roles = List.of();
            }

            // Convert each role string into a SimpleGrantedAuthority, which Spring Security understands
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });
    }

    // Convert the JWT into an Authentication token using the customized converter
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        System.out.println("JWT Claims: " + jwt.getClaims());
        System.out.println("Extracted Authorities: " +
                jwtAuthenticationConverter.convert(jwt).getAuthorities());
        return (AbstractAuthenticationToken) this.jwtAuthenticationConverter.convert(jwt);
    }
}
