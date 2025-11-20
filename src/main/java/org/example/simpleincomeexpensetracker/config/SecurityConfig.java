package org.example.simpleincomeexpensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置
 * 只用 Spring Security 的密碼加密功能，不使用其認證機制
 * 認證由自定義的 JWT 攔截器處理
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密碼加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 禁用 Spring Security 的預設配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 禁用 CSRF（因為使用 JWT）
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll();  // 允許所有請求（由 JWT 攔截器處理認證）

        return http.build();
    }
}