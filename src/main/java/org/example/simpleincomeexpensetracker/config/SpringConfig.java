package org.example.simpleincomeexpensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC 配置類
 * 用於配置 Web 層相關的 Bean 和設定
 */
@Configuration  // 標註此類為 Spring 配置類，會被 Spring 容器識別並載入
@EnableWebMvc  // 啟用 Spring MVC 功能，包含 @RequestMapping 等註解的支援
public class SpringConfig extends WebMvcConfigurationSupport {
    // 繼承 WebMvcConfigurationSupport 以提供 Spring MVC 的基礎配置支援

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
}