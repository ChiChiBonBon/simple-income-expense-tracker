package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.simpleincomeexpensetracker.dto.ApiResponse;
import org.example.simpleincomeexpensetracker.dto.LoginRequestDTO;
import org.example.simpleincomeexpensetracker.dto.RegisterRequestDTO;
import org.example.simpleincomeexpensetracker.entity.Users;
import org.example.simpleincomeexpensetracker.exception.TokenException;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.service.TokenLogoutService;
import org.example.simpleincomeexpensetracker.service.UsersService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 認證控制器
 * 處理登入、註冊、登出、驗證等功能
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "認證 API", description = "使用者登入、註冊、JWT 驗證相關 API")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private TokenLogoutService tokenLogoutService;

    /**
     * 使用者登入
     */
    @PostMapping("/login")
    @Operation(summary = "使用者登入", description = "驗證帳號密碼並回傳 JWT token")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequestDTO request) {
        try {
            log.info("========================================");
            log.info("使用者登入請求: {}", request.getUsername());

            // 驗證帳號密碼
            Users users = usersService.authenticate(request.getUsername(), request.getPassword());

            if (users == null) {
                log.warn("登入失敗：帳號或密碼錯誤");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("帳號或密碼錯誤"));
            }

            // 生成 JWT token
            String token = jwtUtil.generateToken(
                    users.getUserId(),
                    users.getUsername(),
                    request.isRememberMe()
            );

            // 準備回應資料
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("username", users.getUsername());
            data.put("userId", users.getUserId());

            log.info("登入成功，使用者: {}, ID: {}", users.getUsername(), users.getUserId());
            log.info("========================================");

            return ResponseEntity.ok(ApiResponse.success(data, "登入成功"));

        } catch (Exception e) {
            log.error("登入時發生錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("登入時發生錯誤"));
        }
    }

    /**
     * 使用者註冊
     */
    @PostMapping("/register")
    @Operation(summary = "使用者註冊", description = "註冊新使用者帳號")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequestDTO request) {
        try {
            log.info("========================================");
            log.info("使用者註冊請求: {}", request.getUsername());

            // 驗證帳號格式
            if (!request.getUsername().matches("^[a-zA-Z0-9_]{4,20}$")) {
                log.warn("帳號格式不正確: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("帳號格式不正確，請使用 4-20 個英文字母、數字或底線"));
            }

            // 驗證密碼長度
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                log.warn("密碼長度不足");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("密碼長度至少需要 6 個字元"));
            }

            // 檢查帳號是否已存在
            if (usersService.existsByUsername(request.getUsername())) {
                log.warn("帳號已存在: {}", request.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("該帳號已被註冊"));
            }

            // 建立新使用者
            Users newUser = new Users();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));

            usersService.save(newUser);

            log.info("註冊成功，使用者: {}", newUser.getUsername());
            log.info("========================================");

            return ResponseEntity.ok(ApiResponse.success("註冊成功"));

        } catch (Exception e) {
            log.error("註冊時發生錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("註冊時發生錯誤"));
        }
    }

    /**
     * 驗證 Token 是否有效
     */
    @GetMapping("/verify")
    @Operation(summary = "驗證 Token", description = "驗證 JWT token 是否有效")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyToken(HttpServletRequest request) {
        try {
            // 從請求標頭中取得 token
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Token 不存在"));
            }

            String token = authHeader.substring(7);

            // 驗證 token
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Token 無效或已過期"));
            }

            // Token 有效，回傳使用者資訊
            String username = jwtUtil.getUsernameFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("username", username);

            log.info("Token 驗證成功，使用者: {}", username);

            return ResponseEntity.ok(ApiResponse.success(data, "Token 有效"));

        } catch (Exception e) {
            log.error("Token 驗證失敗", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token 驗證失敗"));
        }
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request) {
        try {
            String token =extractToken(request);
            tokenLogoutService.logout(token);
            return ResponseEntity.ok(ApiResponse.success("登出成功"));
        } catch (TokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        // 检查是否以 "Bearer " 开头
        if (header != null && header.startsWith("Bearer ")) {
            // 提取 "Bearer " 之后的 token
            // "Bearer eyJhbGc..." → "eyJhbGc..."
            return header.substring(7);
        }

        return null;
    }
}