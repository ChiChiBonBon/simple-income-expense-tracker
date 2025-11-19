package org.example.simpleincomeexpensetracker.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.simpleincomeexpensetracker.exception.TokenException;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.service.TokenLogoutService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Token 登出服務實現
 * 封裝登出的所有業務邏輯
 */
@Slf4j
@Service
public class TokenLogoutServiceImpl implements TokenLogoutService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional
    public void logout(String token) throws TokenException {
        tokenBlacklistService.addToBlacklist(token);
    }
}
