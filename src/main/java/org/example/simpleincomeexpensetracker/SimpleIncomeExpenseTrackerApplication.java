package org.example.simpleincomeexpensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 應用程式主啟動類別
 * 這是整個應用程式的入口點
 */
@SpringBootApplication
// @SpringBootApplication 是一個複合註解，等同於以下三個註解的組合：
// 1. @SpringBootConfiguration：標註為 Spring Boot 的配置類別
// 2. @EnableAutoConfiguration：啟用 Spring Boot 的自動配置機制
//    - 自動配置 Spring MVC、JPA、資料庫連線等
// 3. @ComponentScan：自動掃描此類別所在套件及其子套件中的 Spring 元件
//    - 掃描 @Controller、@Service、@Repository、@Component 等註解
public class SimpleIncomeExpenseTrackerApplication {

    /**
     * 應用程式主方法（程式入口點）
     * 執行此方法會啟動 Spring Boot 應用程式
     */
    public static void main(String[] args) {
        // 啟動 Spring Boot 應用程式
        // 1. 建立 Spring 容器（ApplicationContext）
        // 2. 載入所有配置
        // 3. 掃描並註冊所有 Spring Bean
        // 4. 啟動內嵌的 Tomcat 伺服器（預設 port 8080）
        // 5. 初始化資料庫連線
        // 6. 準備好接受 HTTP 請求
        SpringApplication.run(SimpleIncomeExpenseTrackerApplication.class, args);
    }
}