package org.example.simpleincomeexpensetracker.controller;

import org.example.simpleincomeexpensetracker.service.TokenLogoutService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 頁面控制器
 * 負責返回 JSP 頁面視圖
 */
@Controller
public class IndexController {

    @Autowired
    private TokenLogoutService tokenLogoutService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 首頁（需要認證）
     */
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    /**
     * 登入頁面（不需要認證）
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 註冊頁面（不需要認證）
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 登出端點 - 重定向到登入頁面
     * 前端的 JWT token 會由 JavaScript 自動清除
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        tokenLogoutService.logout(jwtUtil.extractToken(request));

        HttpSession session = request.getSession();

        session.invalidate();

        // 登出后重定向到登入頁面
        return "redirect:/login";
    }
}