package org.example.simpleincomeexpensetracker.service.impl;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleincomeexpensetracker.entity.TokenBlacklist;
import org.example.simpleincomeexpensetracker.repository.TokenBlacklistRepository;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Token 黑名單服務實現
 */
@Service
@Slf4j
@Transactional
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 添加 token 到黑名單
     */
    public void addToBlacklist(String token) {
        //使用 JwtUtil 的方法獲取過期時間
        //1. 獲得過期時間
        LocalDateTime expiresAt = extractExpirationTime(token);

        //2. 獲得 userId（從 token 中解析出來）
        Integer userId = jwtUtil.getUserIdFromToken(token);

        //3. 生成 token 的 hash
        String tokenHash = jwtUtil.generateTokenHash(token);

        //4. 創建物件並設置所有字段
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);                          //token
        tokenBlacklist.setTokenHash(tokenHash);                  //token hash
        tokenBlacklist.setUserId(userId);                        //userId
        tokenBlacklist.setBlacklistedAt(LocalDateTime.now());   //添加時間
        tokenBlacklist.setExpiresAt(expiresAt);

        // 保存到数据库
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    /**
     * 檢查 token 是否在黑名單中
     */
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    /**
     * 清理所有已過期的 token（登入時用）
     * 根據 token 本身的過期時間判斷
     */
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        long deletedCount = tokenBlacklistRepository.deleteByExpiresAtBefore(now);
        if (deletedCount > 0) {
            log.info("清理了 {} 筆已過期的黑名單記錄", deletedCount);
        }
    }

    /**
     * 從JWT token中提取過期時間
     */
    private LocalDateTime extractExpirationTime(String token) {
        try {
            // 使用JwtUtil提供的方法
            Date expirationDate = jwtUtil.getExpirationDateFromToken(token);

            // 轉換為LocalDateTime
            return expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (JwtException e) {
            // 如果提取失敗，默認設為1小時後
            return LocalDateTime.now().plusHours(1);
        }
    }
}