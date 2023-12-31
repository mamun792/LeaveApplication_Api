package com.example.leave_app.config;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.HashSet;

@Component
public class JwtBlacklist {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}