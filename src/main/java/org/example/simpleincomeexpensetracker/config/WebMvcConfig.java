package org.example.simpleincomeexpensetracker.config;

import org.example.simpleincomeexpensetracker.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Web MVC 配置 - 攔截器配置
 */
@EnableWebMvc // 啟用 Spring MVC 功能，包含 @RequestMapping 等註解的支援
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
                        "/api/auth/register",   // 排除註冊 API
                        "/api/auth/verify"      // 排除驗證 API
                );
    }

    /**
     * 配置視圖解析器 Bean
     * 用於將控制器返回的邏輯視圖名稱解析為實際的 JSP 檔案路徑
     */
    @Bean  // 將此方法的返回值註冊為 Spring 容器管理的 Bean
    public InternalResourceViewResolver viewResolver() {
        // 建立內部資源視圖解析器實例
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        // 設定視圖檔案的前綴路徑：/WEB-INF/view/
        resolver.setPrefix("/WEB-INF/view/");

        // 設定視圖檔案的後綴名：.jsp
        resolver.setSuffix(".jsp");

        // 返回配置好的視圖解析器
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置 /js/** 路徑的靜態資源
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/", "/js/");

        // 配置 /css/** 路徑的靜態資源
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/", "/css/");

        // 配置 /images/** 路徑的靜態資源
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/images/", "/images/");
    }
}