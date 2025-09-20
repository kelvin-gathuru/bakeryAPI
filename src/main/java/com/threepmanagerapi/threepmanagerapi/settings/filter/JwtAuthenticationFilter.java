package com.threepmanagerapi.threepmanagerapi.settings.filter;

import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip JWT check for whitelisted endpoints
        String path = request.getRequestURI();
        if (path.startsWith("/api/user/authenticate") || 
            path.startsWith("/api/user/forgotPassword") ||
            path.startsWith("/api/user/resetPassword") ||
            path.startsWith("/api/user/create")) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // Allow OPTIONS requests to pass without authorization
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring(7); // Remove "Bearer " prefix
                if (jwtService.validateToken(token)) {
                    // Token is valid, continue with the request
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                log.error("Error validating JWT token: ", e);
            }
        }

        // Token is not valid, return an unauthorized response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Invalid or missing token\"}");
    }
}
