package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.simpleincomeexpensetracker.dto.ApiResponse;
import org.example.simpleincomeexpensetracker.entity.ExpenseItem;
import org.example.simpleincomeexpensetracker.service.ExpenseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@Tag(name = "支出項目 REST API", description = "支出項目的查詢、新增、修改、刪除 API")
public class ExpenseItemRestController {

    //建立一個靜態變數Logger
    private final static Logger log = LoggerFactory.getLogger(ExpenseItemRestController.class);

    @Autowired
    private ExpenseItemService expenseItemService;

    @GetMapping("/list")
    @Operation(summary = "查詢支出項目列表", description = "取得所有支出項目")
    public ResponseEntity<ApiResponse<List<ExpenseItem>>> getAllExpenseItems() {
        try {
            //撈所有支出項目
            List<ExpenseItem> expenseList = expenseItemService.findByExpenseItemList();
            //log顯示幾筆支出項目
            log.info("查詢支出項目成功，共 {} 筆", expenseList.size());
            return ResponseEntity.ok(ApiResponse.success(expenseList));
        } catch (Exception e) {
            log.error("查詢支出項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢單一支出項目", description = "根據 ID 查詢支出項目")
    public ResponseEntity<ApiResponse<ExpenseItem>> getExpenseItemById(@PathVariable Long id) {
        try {
            //依據查詢ID找支出項目
            ExpenseItem expenseItem = expenseItemService.findById(id);
            if (expenseItem != null) {
                log.info("查詢支出項目成功，ID: {}", id);
                return ResponseEntity.ok(ApiResponse.success(expenseItem));
            } else {
                log.warn("找不到支出項目，ID: {}", id);
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("找不到 ID 為 " + id + " 的支出項目"));
            }
        } catch (Exception e) {
            log.error("查詢支出項目失敗，ID: {}", id, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "新增支出項目", description = "新增一筆支出記錄")
    public ResponseEntity<ApiResponse<ExpenseItem>> addExpenseItem(@RequestBody ExpenseItem expenseItem) {
        try {
            // 後端卡控是否為null
            if (expenseItem.getAccountDate() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("日期不能為空"));
            }
            // 後端卡控是否為null
            if (expenseItem.getAccountItem() == null || expenseItem.getAccountItem().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("項目名稱不能為空"));
            }
            // 後端卡控是否為null
            if (expenseItem.getAccountAmount() == null || expenseItem.getAccountAmount().intValue() <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("金額必須大於 0"));
            }

            //service儲存
            ExpenseItem savedItem = expenseItemService.save(expenseItem);
            log.info("新增支出項目成功，ID: {}, 項目: {}, 金額: {}",
                    savedItem.getExpenseItemId(),
                    savedItem.getAccountItem(),
                    savedItem.getAccountAmount());
            return ResponseEntity.ok(ApiResponse.success(savedItem, "新增成功"));
        } catch (Exception e) {
            log.error("新增支出項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("新增失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/update")
    @Operation(summary = "修改支出項目", description = "修改支出記錄")
    public ResponseEntity<ApiResponse<ExpenseItem>> updateExpenseItem(@RequestBody ExpenseItem expenseItem) {
        try {
            // 驗證 ID是否為null
            if (expenseItem.getExpenseItemId() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("支出項目 ID 不能為空"));
            }

            // 檢查ID是否存在
            ExpenseItem existingItem = expenseItemService.findById(expenseItem.getExpenseItemId());

            //若找不到資料
            if (existingItem == null) {
                log.warn("找不到要修改的支出項目，ID: {}", expenseItem.getExpenseItemId());
                return ResponseEntity.status(404).body(ApiResponse.error("找不到要修改的支出項目"));
            }

            //後端驗證日期是否為null
            if (expenseItem.getAccountDate() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("日期不能為空"));
            }

            //後端驗證項目名稱是否為null
            if (expenseItem.getAccountItem() == null || expenseItem.getAccountItem().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("項目名稱不能為空"));
            }

            //後端驗證金額不能為null或小於等於零
            if (expenseItem.getAccountAmount() == null || expenseItem.getAccountAmount().intValue() <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("金額必須大於 0"));
            }

            //傳入欲修改的expenseItem
            ExpenseItem updatedItem = expenseItemService.update(expenseItem);
            log.info("修改支出項目成功，ID: {}, 項目: {}, 金額: {}",
                    updatedItem.getExpenseItemId(),
                    updatedItem.getAccountItem(),
                    updatedItem.getAccountAmount());
            return ResponseEntity.ok(ApiResponse.success(updatedItem, "修改成功"));
        } catch (Exception e) {
            log.error("修改支出項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("修改失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "刪除支出項目", description = "根據 ID 刪除支出項目")
    public ResponseEntity<ApiResponse<Void>> deleteExpenseItem(@PathVariable Long id) {
        try {
            // 檢查資料是否存在
            ExpenseItem existingItem = expenseItemService.findById(id);
            if (existingItem == null) {
                log.warn("找不到要刪除的支出項目，ID: {}", id);
                return ResponseEntity.status(404).body(ApiResponse.error("找不到要刪除的支出項目"));
            }

            //若有資料則刪除
            expenseItemService.deleteById(id);
            log.info("刪除支出項目成功，ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success(null, "刪除成功"));
        } catch (Exception e) {
            log.error("刪除支出項目失敗，ID: {}", id, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("刪除失敗: " + e.getMessage()));
        }
    }
}