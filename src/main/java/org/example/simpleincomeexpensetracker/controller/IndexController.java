package org.example.simpleincomeexpensetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 頁面控制器
 * 負責返回 JSP 頁面視圖
 */
@Controller
public class IndexController {

    /**
     * 首頁
     * 不再預先載入數據，改由前端 AJAX 載入
     */
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }
}