package org.example.simpleincomeexpensetracker.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.*;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.service.IncomeItemService;
import org.example.simpleincomeexpensetracker.repository.IncomeItemRepository;

import java.util.Date;
import java.util.List;

/**
 * IncomeItemService 層測試
 *
 * 根據 IncomeItemServiceImpl 的實現編寫
 * 這層主要驗證：
 * ├─ Service 方法調用 Repository 的相應方法
 * ├─ 數據正確保存/更新/刪除/查詢
 * ├─ 事務管理（@Transactional）
 * └─ 日誌記錄（@Slf4j）
 */
@SpringBootTest                    // ← 加載完整應用（Service + Repository）
@ActiveProfiles("test")            // ← 使用 H2 內存數據庫
@Transactional                     // ← 每個測試自動回滾
@DisplayName("IncomeItemService 層測試")
class IncomeItemServiceTest {

    private static final Logger log = LoggerFactory.getLogger(IncomeItemServiceTest.class);

    @Autowired
    private IncomeItemService service;      // ← 測試的 Service

    @Autowired
    private IncomeItemRepository repository;  // ← 驗證數據

    private static final Integer USER_ID = 1;
    private static final Integer OTHER_USER_ID = 2;

    @BeforeEach
    void setUp() {
        // 每個測試前清空數據庫
        repository.deleteAll();
        log.info("============ 測試開始 ============");
    }

    // =============================================
    // 測試 1：save(IncomeItem)
    // =============================================

    @Nested
    @DisplayName("save() 方法測試")
    class SaveTests {

        @Test
        @DisplayName("應該成功保存新項目")
        void testSave_Success() {
            // 準備數據
            IncomeItem item = new IncomeItem();
            item.setUserId(USER_ID);
            item.setAccountItem("月薪");
            item.setAccountAmount(50000);
            item.setAccountDate(new Date());

            log.info("保存項目：{}", item);

            // 執行保存
            IncomeItem saved = service.save(item);

            // 驗證返回的對象
            assertThat(saved).isNotNull();
            assertThat(saved.getIncomeItemId()).isNotNull();  // 自增 ID 被設置
            assertThat(saved.getAccountItem()).isEqualTo("月薪");
            assertThat(saved.getAccountAmount()).isEqualTo(50000);
            assertThat(saved.getUserId()).isEqualTo(USER_ID);

            // 驗證數據庫確實保存了
            assertThat(repository.count()).isEqualTo(1);

            IncomeItem found = repository.findById(saved.getIncomeItemId()).orElse(null);
            assertThat(found.getAccountAmount()).isEqualTo(50000);

            log.info("✅ 保存成功，ID: {}", saved.getIncomeItemId());
        }

        @Test
        @DisplayName("應該支持保存多個項目")
        void testSave_Multiple() {
            // 保存多個項目
            IncomeItem item1 = createItem(USER_ID, "月薪", 50000);
            IncomeItem item2 = createItem(USER_ID, "獎金", 20000);
            IncomeItem item3 = createItem(OTHER_USER_ID, "其他", 10000);

            service.save(item1);
            service.save(item2);
            service.save(item3);

            // 驗證
            assertThat(repository.count()).isEqualTo(3);
            assertThat(service.findByUserId(USER_ID)).hasSize(2);
            assertThat(service.findByUserId(OTHER_USER_ID)).hasSize(1);
        }

        @Test
        @DisplayName("保存金額邊界值")
        void testSave_BoundaryAmounts() {
            Integer[] amounts = { 0, 1, 50000, 999999, Integer.MAX_VALUE };

            for (Integer amount : amounts) {
                IncomeItem item = createItem(USER_ID, "測試" + amount, amount);
                IncomeItem saved = service.save(item);

                assertThat(saved.getAccountAmount()).isEqualTo(amount);
            }

            assertThat(repository.count()).isEqualTo(amounts.length);
        }
    }

    // =============================================
    // 測試 2：update(IncomeItem)
    // =============================================

    @Nested
    @DisplayName("update() 方法測試")
    class UpdateTests {

        @Test
        @DisplayName("應該成功更新存在的項目")
        void testUpdate_ExistingItem() {
            // 準備數據：先保存一個項目
            IncomeItem item = createItem(USER_ID, "月薪", 50000);
            IncomeItem saved = service.save(item);
            Integer itemId = saved.getIncomeItemId();

            // 修改項目
            saved.setAccountAmount(55000);
            saved.setAccountItem("月薪（加薪後）");

            log.info("更新項目: {}", saved);

            // 執行更新
            IncomeItem updated = service.update(saved);

            // 驗證返回的對象
            assertThat(updated.getIncomeItemId()).isEqualTo(itemId);
            assertThat(updated.getAccountAmount()).isEqualTo(55000);
            assertThat(updated.getAccountItem()).isEqualTo("月薪（加薪後）");

            // 驗證數據庫確實更新了
            IncomeItem found = repository.findById(itemId).orElse(null);
            assertThat(found.getAccountAmount()).isEqualTo(55000);

            // 驗證總數沒變
            assertThat(repository.count()).isEqualTo(1);

            log.info("✅ 更新成功");
        }

        @Test
        @DisplayName("應該能將項目分配給不同用戶（實際上是修改 userId）")
        void testUpdate_ChangeUser() {
            // 準備數據
            IncomeItem item = createItem(USER_ID, "月薪", 50000);
            IncomeItem saved = service.save(item);

            // 修改用戶
            saved.setUserId(OTHER_USER_ID);

            IncomeItem updated = service.update(saved);

            // 驗證
            assertThat(updated.getUserId()).isEqualTo(OTHER_USER_ID);
            assertThat(service.findByUserId(USER_ID)).isEmpty();
            assertThat(service.findByUserId(OTHER_USER_ID)).hasSize(1);
        }

        @Test
        @DisplayName("update 沒有 ID 時應作為新增")
        void testUpdate_NewItem() {
            // 準備沒有 ID 的新項目
            IncomeItem newItem = createItem(USER_ID, "獎金", 20000);

            // 執行更新（應該作為新增）
            IncomeItem result = service.update(newItem);

            // 驗證
            assertThat(result.getIncomeItemId()).isNotNull();
            assertThat(repository.count()).isEqualTo(1);
        }
    }

    // =============================================
    // 測試 3：deleteByIncomeItemId(Integer)
    // =============================================

    @Nested
    @DisplayName("deleteByIncomeItemId() 方法測試")
    class DeleteTests {

        @Test
        @DisplayName("應該成功刪除存在的項目")
        void testDelete_Success() {
            // 準備數據
            IncomeItem saved = service.save(createItem(USER_ID, "月薪", 50000));
            Integer itemId = saved.getIncomeItemId();

            assertThat(repository.count()).isEqualTo(1);

            log.info("刪除項目 ID: {}", itemId);

            // 執行刪除
            service.deleteByIncomeItemId(itemId);

            // 驗證
            assertThat(repository.count()).isEqualTo(0);
            assertThat(repository.findById(itemId)).isEmpty();

            log.info("✅ 刪除成功");
        }

        @Test
        @DisplayName("刪除不存在的 ID 不應拋出異常")
        void testDelete_NotFound() {
            // 應該不拋出異常
            assertThatNoException().isThrownBy(() -> {
                service.deleteByIncomeItemId(999);
            });

            log.info("✅ 刪除不存在的 ID 沒有拋出異常");
        }

        @Test
        @DisplayName("應該只刪除指定的項目，不影響其他項目")
        void testDelete_OnlyDeleteTarget() {
            // 準備數據
            IncomeItem item1 = service.save(createItem(USER_ID, "月薪", 50000));
            IncomeItem item2 = service.save(createItem(USER_ID, "獎金", 20000));
            IncomeItem item3 = service.save(createItem(USER_ID, "兼職", 10000));

            assertThat(repository.count()).isEqualTo(3);

            // 刪除中間的一個
            service.deleteByIncomeItemId(item2.getIncomeItemId());

            // 驗證
            assertThat(repository.count()).isEqualTo(2);
            assertThat(repository.findById(item1.getIncomeItemId())).isPresent();
            assertThat(repository.findById(item2.getIncomeItemId())).isEmpty();
            assertThat(repository.findById(item3.getIncomeItemId())).isPresent();
        }
    }

    // =============================================
    // 測試 4：findByUserIdAndAccountDateBetween
    // =============================================

    @Nested
    @DisplayName("findByUserIdAndAccountDateBetween() 方法測試")
    class FindByDateRangeTests {

        @Test
        @DisplayName("應該根據 userId 和日期範圍查詢")
        void testFindByDateRange_Success() {
            // 準備日期
            long now = System.currentTimeMillis();
            Date startDate = new Date(now - 30 * 24 * 60 * 60 * 1000);  // 30 天前
            Date endDate = new Date(now + 30 * 24 * 60 * 60 * 1000);    // 30 天後
            Date outsideDate = new Date(now - 60 * 24 * 60 * 60 * 1000); // 60 天前

            // 準備數據：在範圍內
            IncomeItem item1 = createItem(USER_ID, "月薪", 50000);
            item1.setAccountDate(new Date(now));
            service.save(item1);

            IncomeItem item2 = createItem(USER_ID, "獎金", 20000);
            item2.setAccountDate(startDate);
            service.save(item2);

            // 超出範圍
            IncomeItem item3 = createItem(USER_ID, "舊項目", 5000);
            item3.setAccountDate(outsideDate);
            service.save(item3);

            // 其他用戶
            IncomeItem item4 = createItem(OTHER_USER_ID, "其他", 10000);
            item4.setAccountDate(new Date(now));
            service.save(item4);

            // 執行查詢
            List<IncomeItem> result = service.findByUserIdAndAccountDateBetween(
                    USER_ID, startDate, endDate
            );

            // 驗證
            assertThat(result)
                    .hasSize(2)
                    .allMatch(item -> item.getUserId().equals(USER_ID))
                    .extracting(IncomeItem::getAccountItem)
                    .containsExactlyInAnyOrder("月薪", "獎金");
        }

        @Test
        @DisplayName("日期範圍查詢：無符合項目應返回空列表")
        void testFindByDateRange_NoMatch() {
            long now = System.currentTimeMillis();
            Date startDate = new Date(now - 60 * 24 * 60 * 60 * 1000);
            Date endDate = new Date(now - 30 * 24 * 60 * 60 * 1000);

            // 添加數據（在範圍外）
            IncomeItem item = createItem(USER_ID, "項目", 10000);
            item.setAccountDate(new Date(now));
            service.save(item);

            // 執行查詢
            List<IncomeItem> result = service.findByUserIdAndAccountDateBetween(
                    USER_ID, startDate, endDate
            );

            // 驗證
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("日期邊界：開始日期 = 結束日期")
        void testFindByDateRange_SameDate() {
            Date targetDate = new Date();

            IncomeItem item = createItem(USER_ID, "月薪", 50000);
            item.setAccountDate(targetDate);
            service.save(item);

            List<IncomeItem> result = service.findByUserIdAndAccountDateBetween(
                    USER_ID, targetDate, targetDate
            );

            assertThat(result).hasSize(1);
        }
    }

    // =============================================
    // 測試 5：findByUserId
    // =============================================

    @Nested
    @DisplayName("findByUserId() 方法測試")
    class FindByUserIdTests {

        @Test
        @DisplayName("應該查詢特定用戶的所有項目")
        void testFindByUserId_Success() {
            // 準備數據
            service.save(createItem(USER_ID, "項目 1", 10000));
            service.save(createItem(USER_ID, "項目 2", 20000));
            service.save(createItem(OTHER_USER_ID, "其他用戶", 30000));

            // 執行查詢
            List<IncomeItem> result = service.findByUserId(USER_ID);

            // 驗證
            assertThat(result)
                    .hasSize(2)
                    .allMatch(item -> item.getUserId().equals(USER_ID))
                    .extracting(IncomeItem::getAccountItem)
                    .containsExactlyInAnyOrder("項目 1", "項目 2");
        }

        @Test
        @DisplayName("不存在的用戶應返回空列表")
        void testFindByUserId_NotFound() {
            // 添加其他用戶的數據
            service.save(createItem(OTHER_USER_ID, "項目", 10000));

            // 執行查詢不存在的用戶
            List<IncomeItem> result = service.findByUserId(USER_ID);

            // 驗證
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("應該查詢用戶的大量項目")
        void testFindByUserId_ManyItems() {
            // 為用戶保存 100 個項目
            for (int i = 0; i < 100; i++) {
                service.save(createItem(USER_ID, "項目" + i, 10000 + i));
            }

            // 為其他用戶保存 50 個項目
            for (int i = 0; i < 50; i++) {
                service.save(createItem(OTHER_USER_ID, "其他" + i, 20000 + i));
            }

            // 執行查詢
            List<IncomeItem> result = service.findByUserId(USER_ID);

            // 驗證
            assertThat(result)
                    .hasSize(100)
                    .allMatch(item -> item.getUserId().equals(USER_ID));
        }
    }

    // =============================================
    // 測試 6：findByIncomeItemIdAndUserId
    // =============================================

    @Nested
    @DisplayName("findByIncomeItemIdAndUserId() 方法測試")
    class FindByIdAndUserIdTests {

        @Test
        @DisplayName("應該查詢特定用戶的特定項目")
        void testFindByIdAndUserId_Success() {
            // 準備數據
            IncomeItem saved = service.save(createItem(USER_ID, "月薪", 50000));

            // 執行查詢
            IncomeItem result = service.findByIncomeItemIdAndUserId(
                    saved.getIncomeItemId(), USER_ID
            );

            // 驗證
            assertThat(result)
                    .isNotNull()
                    .extracting(IncomeItem::getAccountItem)
                    .isEqualTo("月薪");
            assertThat(result.getAccountAmount()).isEqualTo(50000);
        }

        @Test
        @DisplayName("不匹配的 userId 應返回 null")
        void testFindByIdAndUserId_WrongUser() {
            // 準備數據
            IncomeItem saved = service.save(createItem(USER_ID, "月薪", 50000));

            // 執行查詢（用錯誤的 userId）
            IncomeItem result = service.findByIncomeItemIdAndUserId(
                    saved.getIncomeItemId(), OTHER_USER_ID
            );

            // 驗證
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("不存在的 ID 應返回 null")
        void testFindByIdAndUserId_NotFound() {
            IncomeItem result = service.findByIncomeItemIdAndUserId(999, USER_ID);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("ID 和 userId 都不匹配應返回 null")
        void testFindByIdAndUserId_BothNotMatch() {
            IncomeItem result = service.findByIncomeItemIdAndUserId(999, 999);

            assertThat(result).isNull();
        }
    }

    // =============================================
    // 額外測試：綜合場景
    // =============================================

    @Nested
    @DisplayName("綜合場景測試")
    class IntegrationScenarios {

        @Test
        @DisplayName("完整的增刪改查流程")
        void testCompleteLifecycle() {
            log.info("========== 開始完整流程測試 ==========");

            // 1. 保存
            IncomeItem item = createItem(USER_ID, "月薪", 50000);
            IncomeItem saved = service.save(item);
            log.info("✅ 保存成功，ID: {}", saved.getIncomeItemId());
            assertThat(repository.count()).isEqualTo(1);

            // 2. 查詢
            List<IncomeItem> allItems = service.findByUserId(USER_ID);
            log.info("✅ 查詢成功，共 {} 項", allItems.size());
            assertThat(allItems).hasSize(1);

            // 3. 更新
            saved.setAccountAmount(55000);
            IncomeItem updated = service.update(saved);
            log.info("✅ 更新成功，新金額: {}", updated.getAccountAmount());
            assertThat(updated.getAccountAmount()).isEqualTo(55000);

            // 4. 刪除
            service.deleteByIncomeItemId(saved.getIncomeItemId());
            log.info("✅ 刪除成功");
            assertThat(repository.count()).isEqualTo(0);

            log.info("========== 完整流程測試結束 ==========");
        }

        @Test
        @DisplayName("多用戶場景：數據隔離")
        void testMultiUserDataIsolation() {
            // User 1 的項目
            for (int i = 1; i <= 5; i++) {
                service.save(createItem(1, "User1 Item" + i, 10000 * i));
            }

            // User 2 的項目
            for (int i = 1; i <= 3; i++) {
                service.save(createItem(2, "User2 Item" + i, 20000 * i));
            }

            // User 3 的項目
            for (int i = 1; i <= 2; i++) {
                service.save(createItem(3, "User3 Item" + i, 30000 * i));
            }

            // 驗證
            assertThat(service.findByUserId(1)).hasSize(5);
            assertThat(service.findByUserId(2)).hasSize(3);
            assertThat(service.findByUserId(3)).hasSize(2);

            // 刪除 User 1 的一個項目
            IncomeItem toDelete = service.findByUserId(1).get(0);
            service.deleteByIncomeItemId(toDelete.getIncomeItemId());

            // 驗證只有 User 1 受影響
            assertThat(service.findByUserId(1)).hasSize(4);
            assertThat(service.findByUserId(2)).hasSize(3);  // 不受影響
            assertThat(service.findByUserId(3)).hasSize(2);  // 不受影響
        }
    }

    // =============================================
    // 輔助方法
    // =============================================

    /**
     * 創建 IncomeItem 的輔助方法
     */
    private IncomeItem createItem(Integer userId, String accountItem, Integer accountAmount) {
        IncomeItem item = new IncomeItem();
        item.setUserId(userId);
        item.setAccountItem(accountItem);
        item.setAccountAmount(accountAmount);
        item.setAccountDate(new Date());
        return item;
    }
}