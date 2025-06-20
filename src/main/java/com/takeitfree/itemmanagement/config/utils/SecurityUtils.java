package com.takeitfree.itemmanagement.config.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtils {

    /**
     * Utility method to retrieve the ID of the currently authenticated user
     * based on the JWT token stored in the Spring Security context.
     *
     * @return the user ID from the "userId" claim in the JWT
     * @throws IllegalStateException if the user is not authenticated via JWT or if the ID is missing
     */
    public static Long getCurrentUserId() {
        //get info of the user connected
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {

            Jwt jwt = jwtAuth.getToken();

            //recover id of the user
            Object userIdClaim = jwt.getClaim("userId");

            //transform id to Long
            if (userIdClaim != null) {
                return Long.parseLong(userIdClaim.toString());
            }
        }

        throw new IllegalStateException("User ID not found in JWT token");
    }

    public static String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Authorization header is missing or invalid");
    }
}
