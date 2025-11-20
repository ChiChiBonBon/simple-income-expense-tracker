package org.example.simpleincomeexpensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.repository.IncomeItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional  // ✅ 每個測試自動回滾
@DisplayName("收入項目 Controller 集成測試")
class IncomeItemRestControllerTest {

    // ========== 依賴注入 ==========

    @Autowired
    private MockMvc mockMvc;  // ✅ 用於發送 HTTP 請求

    @Autowired
    private IncomeItemRepository incomeItemRepository;  // ✅ 真實的 Repository

    @Autowired
    private ObjectMapper objectMapper;  // ✅ 用於序列化/反序列化 JSON

    // ========== 測試數據 ==========

    private IncomeItem testIncomeItem;

    /**
     * 每個測試前執行，準備測試數據
     */
    @BeforeEach
    void setUp() {
        // 清空表（可選，@Transactional 會自動回滾）
        incomeItemRepository.deleteAll();

        // 創建測試數據（真實保存到 H2 數據庫）
        testIncomeItem = new IncomeItem();
        testIncomeItem.setAccountItem("普發十萬");
        testIncomeItem.setAccountAmount(100000);
        testIncomeItem.setAccountDate(new Date());
        testIncomeItem.setIncomeItemId(99);
        testIncomeItem.setUserId(1);

        //保存到真實數據庫
        testIncomeItem = incomeItemRepository.save(testIncomeItem);
    }


}
