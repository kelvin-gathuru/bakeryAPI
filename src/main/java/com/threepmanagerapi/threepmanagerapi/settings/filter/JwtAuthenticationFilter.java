package com.threepmanagerapi.threepmanagerapi.settings.filter;

import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if ("OPTIONS".equals(request.getMethod())) {
            // Allow OPTIONS requests to pass without authorization
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");

        // Check if the token is valid
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
            if (jwtService.validateToken(token)) {
                // Token is valid, continue with the request
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Token is not valid, return an unauthorized response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
    }
}
