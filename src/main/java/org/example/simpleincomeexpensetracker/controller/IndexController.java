package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.simpleincomeexpensetracker.service.TokenBlacklistService;
import org.example.simpleincomeexpensetracker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

/**
 * 頁面控制器
 * 負責返回 JSP 頁面視圖
 */
@Slf4j
@Controller
@Tag(name = "頁面路由", description = "JSP 頁面路由控制器")
public class IndexController {

    /**
     * 首頁（需要認證）
     */
    @GetMapping({"/", "/index"})
    @Operation(summary = "首頁", description = "返回應用首頁（需要身份驗證）")
    public String index() {
        return "index";
    }

    /**
     * 登入頁面（不需要認證）
     */
    @GetMapping("/login")
    @Operation(summary = "登入頁面", description = "返回登入頁面視圖")
    public String login() {
        return "login";
    }

    /**
     * 註冊頁面（不需要認證）
     */
    @GetMapping("/register")
    @Operation(summary = "註冊頁面", description = "返回用戶註冊頁面視圖")
    public String register() {
        return "register";
    }
}