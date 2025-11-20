package org.example.simpleincomeexpensetracker.service;

public interface TokenBlacklistService {
    void addToBlacklist(String token);
    boolean isTokenBlacklisted(String token);
    void cleanExpiredTokens();
}
