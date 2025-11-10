package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.simpleincomeexpensetracker.dto.ApiResponse;
import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.service.IncomeItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@Tag(name = "收入項目 REST API", description = "收入項目的查詢、新增、修改、刪除 API")
public class IncomeItemRestController {

    //建立靜態變數log
    private final static Logger log = LoggerFactory.getLogger(IncomeItemRestController.class);

    @Autowired
    private IncomeItemService incomeItemService;

    @GetMapping("/list")
    @Operation(summary = "查詢收入項目列表", description = "取得所有收入項目")
    public ResponseEntity<ApiResponse<List<IncomeItem>>> getAllIncomeItems() {
        try {
            //找出所有收入項目
            List<IncomeItem> incomeList = incomeItemService.findByIncomeItemList();
            //log顯示幾筆收入項目
            log.info("查詢收入項目成功，共 {} 筆", incomeList.size());
            return ResponseEntity.ok(ApiResponse.success(incomeList));
        } catch (Exception e) {
            log.error("查詢收入項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "查詢單一收入項目", description = "根據 ID 查詢收入項目")
    public ResponseEntity<ApiResponse<IncomeItem>> getIncomeItemById(@PathVariable Long id) {
        try {
            //依據收入項目的ID查詢
            IncomeItem incomeItem = incomeItemService.findById(id);

            //查詢到的incomeItem是否是null
            if (incomeItem != null) {
                //log顯示收入項目
                log.info("查詢收入項目成功，ID: {}", id);
                return ResponseEntity.ok(ApiResponse.success(incomeItem));
            } else {
                log.warn("找不到收入項目，ID: {}", id);
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("找不到 ID 為 " + id + " 的收入項目"));
            }
        } catch (Exception e) {
            log.error("查詢收入項目失敗，ID: {}", id, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "新增收入項目", description = "新增一筆收入記錄")
    public ResponseEntity<ApiResponse<IncomeItem>> addIncomeItem(@RequestBody IncomeItem incomeItem) {
        try {
            //新增收入項目的金額是否等於null或小於等於零
            if (incomeItem.getAccountAmount() == null || incomeItem.getAccountAmount().intValue() <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("金額必須大於 0"));
            }

            //儲存收入項目
            IncomeItem savedItem = incomeItemService.save(incomeItem);
            log.info("新增收入項目成功，ID: {}", savedItem.getIncomeItemId());
            return ResponseEntity.ok(ApiResponse.success(savedItem, "新增成功"));
        } catch (Exception e) {
            log.error("新增收入項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("新增失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/update")
    @Operation(summary = "修改收入項目", description = "修改收入記錄")
    public ResponseEntity<ApiResponse<IncomeItem>> updateIncomeItem(@RequestBody IncomeItem incomeItem) {
        try {
            //判斷incomeItem的id是否是null
            if (incomeItem.getIncomeItemId() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("收入項目 ID 不能為空"));
            }

            //找欲修改的id之existingItem
            IncomeItem existingItem = incomeItemService.findById(incomeItem.getIncomeItemId());

            //判斷existingItem是否等於null
            if (existingItem == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("找不到要修改的收入項目"));
            }

            //傳入欲修改的existingItem
            IncomeItem updatedItem = incomeItemService.update(incomeItem);

            //log顯示修改成功後的id
            log.info("修改收入項目成功，ID: {}", updatedItem.getIncomeItemId());
            return ResponseEntity.ok(ApiResponse.success(updatedItem, "修改成功"));
        } catch (Exception e) {
            log.error("修改收入項目失敗", e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("修改失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "刪除收入項目", description = "根據 ID 刪除收入項目")
    public ResponseEntity<ApiResponse<Void>> deleteIncomeItem(@PathVariable Long id) {
        try {
            //查找existingItem
            IncomeItem existingItem = incomeItemService.findById(id);
            if (existingItem == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("找不到要刪除的收入項目"));
            }

            //依據id執行刪除incomeItem
            incomeItemService.deleteById(id);

            //顯示刪除成功後的收入項目id
            log.info("刪除收入項目成功，ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success(null, "刪除成功"));
        } catch (Exception e) {
            log.error("刪除收入項目失敗，ID: {}", id, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("刪除失敗: " + e.getMessage()));
        }
    }
}