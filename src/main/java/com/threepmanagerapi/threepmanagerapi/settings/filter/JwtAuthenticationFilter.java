package com.threepmanagerapi.threepmanagerapi.settings.filter;

import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Allow authentication endpoints without token validation
        if (isAuthenticationEndpoint(requestURI) || "OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        // Check if the token is valid
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            if (jwtService.validateToken(token)) {
                try {
                    // Extract user information from token
                    String email = jwtService.extractEmail(token);
                    Long userID = jwtService.extractuserID(token);

                    // Create authentication object
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.emptyList() // No authorities needed for basic auth
                    );

                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Token is valid and context is set, continue with the request
                    filterChain.doFilter(request, response);
                    return;
                } catch (Exception e) {
                    // If there's any error extracting user info, treat as unauthorized
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized");
                    return;
                }
            }
        }

        // Token is not valid, return an unauthorized response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
    }

    private boolean isAuthenticationEndpoint(String requestURI) {
        return requestURI.equals("/api/user/authenticate") ||
               requestURI.equals("/api/user/create") ||
               requestURI.equals("/api/user/forgotPassword") ||
               requestURI.equals("/api/user/resetPassword");
    }
}