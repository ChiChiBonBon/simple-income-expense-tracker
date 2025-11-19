package org.example.simpleincomeexpensetracker.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.simpleincomeexpensetracker.entity.Users;
import org.example.simpleincomeexpensetracker.repository.UsersRepository;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 攔截器
 * 用於驗證每個請求的 JWT token
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String token = jwtUtil.extractToken(request);

        if (token == null) {
            return true;
        }

        // 1. 檢查 token 是否在黑名單中
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            log.warn("請求使用了已注銷的 token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"Token已被注銷，請重新登錄\"}");
            return false;
        }

        // 驗證簽名和過期時間
        if (!jwtUtil.validateToken(token)) {
            throw new Exception("Token 無效");
        }

        Integer userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            // Token 失效！用戶已刪除
            throw new Exception("用戶不存在，Token 已失效");
        }

        return true;
    }
}