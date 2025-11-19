package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.exception.TokenException;

public interface TokenLogoutService {
    void logout(String token) throws TokenException;
}