package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.simpleincomeexpensetracker.dto.AccountDataDTO;
import org.example.simpleincomeexpensetracker.dto.ApiResponse;
import org.example.simpleincomeexpensetracker.entity.ExpenseItem;
import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.service.ExpenseItemService;
import org.example.simpleincomeexpensetracker.service.IncomeItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@Tag(name = "記帳資料 REST API", description = "收入和支出的綜合查詢與篩選")
public class AccountRestController {

    private final Logger log = LoggerFactory.getLogger(AccountRestController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private IncomeItemService incomeItemService;

    @Autowired
    private ExpenseItemService expenseItemService;

    @GetMapping("/list")
    @Operation(summary = "查詢所有記帳資料", description = "取得所有收入和支出項目")
    public ResponseEntity<ApiResponse<AccountDataDTO>> getAllAccountData(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("未授權"));
        }

        try {
            //log顯示
            log.info("========================================");
            log.info("開始查詢所有記帳資料");

            // 查詢收入列表
            List<IncomeItem> incomeList = incomeItemService.findByUserId(userId);
            log.info("收入列表查詢成功，共 {} 筆", incomeList.size());

            // 查詢支出列表
            List<ExpenseItem> expenseList = expenseItemService.findByUserId(userId);
            log.info("支出列表查詢成功，共 {} 筆", expenseList.size());

            // 建立 DTO
            AccountDataDTO data = new AccountDataDTO(incomeList, expenseList);

            log.info("回傳資料: 收入 {} 筆, 支出 {} 筆", incomeList.size(), expenseList.size());
            log.info("========================================");

            return ResponseEntity.ok(ApiResponse.success(data, "查詢成功"));

        } catch (Exception e) {
            log.error("========================================");
            log.error("查詢記帳資料失敗", e);
            log.error("========================================");

            // 發生錯誤時回傳空資料
            AccountDataDTO emptyData = new AccountDataDTO(new ArrayList<>(), new ArrayList<>());
            return ResponseEntity.ok(ApiResponse.success(emptyData, "查詢失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "篩選記帳資料", description = "根據日期範圍篩選收入和支出")
    public ResponseEntity<ApiResponse<AccountDataDTO>> filterAccountData(
            @Parameter(description = "開始日期 (yyyy-MM-dd)", example = "2025-01-01")
            @RequestParam(required = false) String startDate,

            @Parameter(description = "結束日期 (yyyy-MM-dd)", example = "2025-12-31")
            @RequestParam(required = false) String endDate,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getAttribute("userId");
        try {
            if(userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(ApiResponse.error("使用者未授權"));
            }

            log.info("userId:{}，篩選記帳資料：開始日期 = {}, 結束日期 = {}",userId, startDate, endDate);

            List<IncomeItem> incomeList;
            List<ExpenseItem> expenseList;

            //判斷開始日期和結束日期是否為空值
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                // 手動轉換字串為 Date
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);

                // 有日期範圍
                incomeList = incomeItemService.findByUserIdAndAccountDateBetween(userId,start,end);
                expenseList = expenseItemService.findByUserIdAndAccountDateBetween(userId,start, end);

                //記錄log
                log.info("日期範圍：{} 到 {}", dateFormat.format(start), dateFormat.format(end));
            } else {
                // 無篩選，篩選所有資料
                incomeList = incomeItemService.findByUserId(userId);
                expenseList = expenseItemService.findByUserId(userId);
            }

            //log記錄
            log.info("篩選結果：收入 {} 筆，支出 {} 筆", incomeList.size(), expenseList.size());

            //建一個回傳的dto
            AccountDataDTO data = new AccountDataDTO(incomeList, expenseList);

            //回傳ok
            return ResponseEntity.ok(ApiResponse.success(data, "篩選成功"));
        }
        //轉換錯誤
        catch (ParseException e) {
            log.error("日期格式錯誤", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error("日期格式錯誤，請使用 yyyy-MM-dd 格式（如：2025-01-01）"));
        }
        //其他的報錯
        catch (Exception e) {
            log.error("篩選記帳資料失敗", e);
            AccountDataDTO emptyData = new AccountDataDTO(new ArrayList<>(), new ArrayList<>());
            return ResponseEntity.ok(ApiResponse.success(emptyData, "篩選失敗: " + e.getMessage()));
        }
    }
}