package org.example.simpleincomeexpensetracker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleincomeexpensetracker.entity.TokenBlacklist;
import org.example.simpleincomeexpensetracker.repository.TokenBlacklistRepository;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Token 黑名單服務實現 - 改進版本
 * addToBlacklist 不再拋出異常，而是無聲忽略重複項
 * 這樣 TokenLogoutService 可以直接調用，無需先檢查
 */
@Slf4j
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * 將 token 加入黑名單
     */
    @Override
    @Transactional
    public void addToBlacklist(String token) {
        try {
            String tokenHash = hashToken(token);

            //如果已存在，直接返回
            if (tokenBlacklistRepository.existsByTokenHash(tokenHash)) {
                log.debug("Token 已在黑名單中，無需重複添加");
                return;
            }

            Integer userId = jwtUtil.getUserIdFromToken(token);

            // 建立黑名單記錄
            TokenBlacklist blacklist = new TokenBlacklist(tokenHash,userId);
            tokenBlacklistRepository.save(blacklist);
            log.info("Token 已加入黑名單");

        } catch (Exception e) {
            log.error("加入黑名單時出錯", e);
            throw new RuntimeException("無法加入黑名單", e);
        }
    }

    /**
     * 檢查 token 是否在黑名單中
     */
    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            String tokenHash = hashToken(token);
            return tokenBlacklistRepository.existsByTokenHash(tokenHash);
        } catch (Exception e) {
            log.error("檢查黑名單時出錯", e);
            // 為了安全起見，如果發生錯誤就視為黑名單中
            return true;
        }
    }

    /**
     * 清理過期的黑名單記錄
     */
    @Override
    public void cleanExpiredBlacklist(int daysOld) {
        try {
            LocalDateTime expirationTime = LocalDateTime.now().minusDays(daysOld);
            tokenBlacklistRepository.deleteByBlacklistedAtBefore(expirationTime);
            log.info("已清理 {} 天前的黑名單記錄", daysOld);
        } catch (Exception e) {
            log.error("清理黑名單時出錯", e);
        }
    }

    /**
     * 使用 SHA-256 對 token 進行雜湊處理
     */
    private String hashToken(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hash = digest.digest(token.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}