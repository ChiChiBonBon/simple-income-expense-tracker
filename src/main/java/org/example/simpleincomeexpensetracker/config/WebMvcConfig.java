package org.example.simpleincomeexpensetracker.config;

import org.example.simpleincomeexpensetracker.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置 - 攔截器配置
 */
@Configuration// 標註此類為 Spring 配置類，會被 Spring 容器識別並載入
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // 攔截所有 /api/** 路徑
                .excludePathPatterns(
                        "/api/auth/login",      // 排除登入 API
                        "/api/auth/register"   // 排除註冊 API
                );
    }
}