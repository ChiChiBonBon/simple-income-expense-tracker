package org.example.simpleincomeexpensetracker;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet 初始化器
 * 用於將 Spring Boot 應用程式部署到外部 Servlet 容器（如 Tomcat、Jetty）
 *
 * 使用場景：
 * 1. 將應用程式打包成 WAR 檔案部署到外部 Tomcat 伺服器
 * 2. 在傳統的 Java EE 容器中運行 Spring Boot 應用
 *
 * 如果使用內嵌 Tomcat（打包成 JAR），則不需要此類別
 */
public class ServletInitializer extends SpringBootServletInitializer {
    // 繼承 SpringBootServletInitializer 讓 Spring Boot 應用程式可以在外部 Servlet 容器中運行

    /**
     * 配置 Spring Boot 應用程式
     * 當應用程式部署到外部 Servlet 容器時，容器會呼叫此方法來啟動 Spring Boot
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // 指定 Spring Boot 的主要配置類別（啟動類別）
        // 等同於在 main 方法中執行 SpringApplication.run(SimpleIncomeExpenseTrackerApplication.class, args)
        return application.sources(SimpleIncomeExpenseTrackerApplication.class);
    }
}