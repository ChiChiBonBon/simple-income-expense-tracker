package org.example.simpleincomeexpensetracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.simpleincomeexpensetracker.dto.ApiResponse;
import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.service.IncomeItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Operation(summary = "使用者查詢收入項目列表", description = "使用者取得所有收入項目")
    public ResponseEntity<ApiResponse<List<IncomeItem>>> getAllIncomeItems(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        try {
            if(userId == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("使用者未授權"));
            }
            //找出所有收入項目
            List<IncomeItem> incomeList = incomeItemService.findByUserId(userId);
            //log顯示幾筆收入項目
            log.info("查詢收入項目成功，userId: {}，共 {} 筆",userId, incomeList.size());
            return ResponseEntity.ok(ApiResponse.success(incomeList));
        } catch (Exception e) {
            log.error("查詢收入項目失敗，userId: {}",userId, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @GetMapping("/{incomeItemId}")
    @Operation(summary = "查詢單一收入項目", description = "根據 ID 查詢收入項目")
    public ResponseEntity<ApiResponse<IncomeItem>> getIncomeItemById(@PathVariable Integer incomeItemId, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        try {

            if(userId == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("使用者未授權"));
            }

            //依據收入項目的ID查詢
            IncomeItem incomeItem = incomeItemService.findByIncomeItemIdAndUserId(incomeItemId, userId);

            //查詢到的incomeItem是否是null
            if (incomeItem != null) {
                //log顯示收入項目
                log.info("查詢收入項目成功，userId: {}，incomeItemId: {}",userId, incomeItemId);
                return ResponseEntity.ok(ApiResponse.success(incomeItem));
            } else {
                log.warn("找不到收入項目，userId: {}，incomeItemId: {}",userId, incomeItemId);
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("找不到 ID 為 " + incomeItemId + " 的收入項目"));
            }
        } catch (Exception e) {
            log.error("查詢收入項目失敗，userId: {}，incomeItemId: {}",userId, incomeItemId, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("查詢失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "使用者新增收入項目", description = "使用者新增一筆收入記錄")
    public ResponseEntity<ApiResponse<IncomeItem>> addIncomeItem(@RequestBody IncomeItem incomeItem,
                                                                 HttpServletRequest request) {
        //從JWT的攔截器中取得userId
        Integer userId = (Integer) request.getAttribute("userId");
        try {
            //後端檢驗userId
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("使用者未認證"));
            }

            //強制設定userId，防止前端偽造
            incomeItem.setUserId(userId);

            //新增收入項目的金額是否等於null或小於等於零
            if (incomeItem.getAccountAmount() == null || incomeItem.getAccountAmount() <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("金額必須大於 0"));
            }

            //儲存收入項目
            IncomeItem savedItem = incomeItemService.save(incomeItem);
            log.info("新增收入項目成功，userId: {}，IncomeItemId: {}", userId,savedItem.getIncomeItemId());
            return ResponseEntity.ok(ApiResponse.success(savedItem, "新增成功"));
        } catch (Exception e) {
            log.error("新增收入項目失敗，userId: {}，incomeItemId: {}",userId,incomeItem.getIncomeItemId(), e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("新增失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/update")
    @Operation(summary = "使用者修改收入項目", description = "使用者修改收入記錄")
    public ResponseEntity<ApiResponse<IncomeItem>> updateIncomeItem(@RequestBody IncomeItem incomeItem,
                                                                    HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        try {

            if(userId == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("使用者未認證"));
            }

            //強制設定userId，防止前端偽造
            incomeItem.setUserId(userId);

            //判斷incomeItem的id是否是null
            if (incomeItem.getIncomeItemId() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("收入項目 ID 不能為空"));
            }

            //找欲修改的id之existingItem
            IncomeItem existingItem = incomeItemService.findByIncomeItemIdAndUserId(incomeItem.getIncomeItemId(), userId);

            //判斷existingItem是否等於null
            if (existingItem == null) {
                return ResponseEntity.status(404)
                                     .body(ApiResponse.error("找不到要修改的收入項目"));
            }

            //傳入欲修改的existingItem
            IncomeItem updatedItem = incomeItemService.update(incomeItem);

            //log顯示修改成功後的id
            log.info("修改收入項目成功，userId: {}，IncomeItemId: {}", userId,updatedItem.getIncomeItemId());
            return ResponseEntity.ok(ApiResponse.success(updatedItem, "修改成功"));
        } catch (Exception e) {
            log.error("修改收入項目失敗，userId: {}，incomeItemId: {}",userId, incomeItem.getIncomeItemId(), e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("修改失敗: " + e.getMessage()));
        }
    }

    @PostMapping("/delete/{incomeItemId}")
    @Operation(summary = "使用者刪除收入項目", description = "根據 ID 刪除收入項目")
    public ResponseEntity<ApiResponse<Void>> deleteIncomeItem(@PathVariable Integer incomeItemId,
                                                              HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        try {

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(ApiResponse.error("使用者未認證"));
            }

            //查找existingItem
            IncomeItem existingItem = incomeItemService.findByIncomeItemIdAndUserId(incomeItemId, userId);
            if (existingItem == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("找不到要刪除的收入項目"));
            }

            //依據id執行刪除incomeItem
            incomeItemService.deleteByIncomeItemId(incomeItemId);

            //顯示刪除成功後的收入項目id
            log.info("刪除收入項目成功，userId: {}，incomeItemId: {}",userId, incomeItemId);
            return ResponseEntity.ok(ApiResponse.success(null, "刪除成功"));
        } catch (Exception e) {
            log.error("刪除收入項目失敗，userId: {}，incomeItemId: {}",userId, incomeItemId, e);
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("刪除失敗: " + e.getMessage()));
        }
    }
}