package org.example.simpleincomeexpensetracker.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

/**
 * JWT 工具類
 * 用於生成、解析和驗證 JWT token
 */
@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    // JWT 密鑰（從配置檔案讀取）
    @Value("${jwt.secret:MySecretKeyForJWTTokenGenerationAndValidation1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ}")
    private String secret;

    // Access Token 有效期（毫秒）- 短期，推薦 15 分鐘 900000ms
    @Value("${jwt.access-token.expiration:900000}")
    private long accessTokenExpiration;  // 15 * 60 * 1000 = 900000

    // Refresh Token 有效期（毫秒）- 長期，推薦 30 天
    @Value("${jwt.refresh-token.expiration:2592000000}")
    private long refreshTokenExpiration;  // 30 * 24 * 60 * 60 * 1000

    // 用於計算 Token 快要過期的臨界值（毫秒）- 推薦 5 分鐘前提示
    @Value("${jwt.expiration-threshold:300000}")
    private long expirationThreshold;

    /**
     * 取得簽章密鑰
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 JWT token
     */
    public String generateToken(Integer userId, String username, boolean rememberMe) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (rememberMe ? refreshTokenExpiration : accessTokenExpiration));

        log.info("生成 JWT token，使用者: {}, 過期時間: {}", username, expiryDate);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 從 token 中取得使用者名稱
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 從 token 中取得使用者 ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Integer.class);
    }

    /**
     * 解析 token 取得 Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 驗證 token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token 已過期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("不支援的 Token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Token 格式錯誤: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("Token 簽章錯誤: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Token 為空: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 檢查 token 是否過期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 刷新 token（生成新的 token）
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String username = claims.getSubject();
            Integer userId = claims.get("userId", Integer.class);

            // 生成新的 token（使用預設過期時間）
            return generateToken(userId, username, false);
        } catch (JwtException e) {
            throw new RuntimeException("無法刷新 token", e);
        }
    }

    /**
     * 從request中提取token
     */
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}