package com.threepmanagerapi.threepmanagerapi.settings.utility;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGenerator {

    public String generateToken() {
        // Generate a random token, e.g., UUID
        return UUID.randomUUID().toString();
    }
}
