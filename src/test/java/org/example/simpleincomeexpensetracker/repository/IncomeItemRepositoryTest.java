package org.example.simpleincomeexpensetracker.repository;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * IncomeItemRepository 測試
 *
 * Entity 字段：
 * - incomeItemId: 主鍵（自增）
 * - accountDate: 記帳日期
 * - accountItem: 項目名稱
 * - accountAmount: 金額（Integer）
 * - userId: 用戶 ID
 */
@SpringBootTest
@DataJpaTest                    // ← 只加載 JPA 配置，速度最快
@ActiveProfiles("test")         // ← 使用 H2 內存數據庫
@DisplayName("IncomeItemRepository 測試")
class IncomeItemRepositoryTest {

    @Autowired
    private IncomeItemRepository repository;

    @Autowired
    private TestEntityManager entityManager;  // ← 用來準備測試數據

    // 測試數據
    private static final Integer USER_ID = 1;
    private static final Integer OTHER_USER_ID = 2;

    @BeforeEach
    void setUp() {
        IncomeItem item1 = new IncomeItem();
        item1.setUserId(1);
        item1.setAccountItem("月薪");
        item1.setAccountAmount(50000);

        IncomeItem item2 = new IncomeItem();
        item2.setUserId(2);
        item2.setAccountItem("年薪");
        item2.setAccountAmount(1000000);

        IncomeItem item3 = new IncomeItem();
        item3.setUserId(3);
        item3.setAccountItem("日薪");
        item3.setAccountAmount(2000);
        repository.save(item3);
    }

    // =============================================
    // 測試 1：findByUserId(Integer userId)
    // =============================================

    @Test
    @DisplayName("應該根據 userId 查詢所有收入項目")
    void testFindByUserId_Success() {
        // 準備數據
        IncomeItem item1 = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(item1);

        IncomeItem item2 = createIncomeItem(USER_ID, "獎金", 20000);
        entityManager.persistAndFlush(item2);

        // 為其他用戶添加數據（不應該被查到）
        IncomeItem item3 = createIncomeItem(OTHER_USER_ID, "其他用戶的收入", 10000);
        entityManager.persistAndFlush(item3);

        // 執行查詢
        List<IncomeItem> result = repository.findByUserId(USER_ID);

        // 驗證：應該只查到自己的 2 個項目
        assertThat(result)
                .hasSize(2)
                .extracting(IncomeItem::getAccountItem)
                .containsExactlyInAnyOrder("月薪", "獎金");

        // 驗證：所有項目都屬於該用戶
        assertThat(result)
                .allMatch(item -> item.getUserId().equals(USER_ID));
    }

    @Test
    @DisplayName("查詢不存在的 userId 應返回空列表")
    void testFindByUserId_NotFound() {
        // 添加一些數據
        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(item);

        // 查詢不存在的用戶
        List<IncomeItem> result = repository.findByUserId(999);

        // 驗證
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("用戶沒有任何收入項目應返回空列表")
    void testFindByUserId_EmptyUser() {
        List<IncomeItem> result = repository.findByUserId(USER_ID);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("同一用戶有多個項目應全部返回")
    void testFindByUserId_Multiple() {
        // 準備 5 個項目
        for (int i = 1; i <= 3; i++) {
            IncomeItem item = createIncomeItem(USER_ID, "項目" + i, 10000 * i);
            entityManager.persistAndFlush(item);
        }

        // 執行查詢
        List<IncomeItem> result = repository.findByUserId(USER_ID);

        // 驗證
        assertThat(result)
                .hasSize(3)
                .extracting(IncomeItem::getAccountAmount)
                .containsExactlyInAnyOrder(10000, 20000, 30000, 40000, 50000);
    }

    // =============================================
    // 測試 2：findByUserIdAndAccountDateBetween
    // =============================================

    @Test
    @DisplayName("應該根據 userId 和日期範圍查詢收入項目")
    void testFindByUserIdAndAccountDateBetween_Success() {
        // 準備日期
        long now = System.currentTimeMillis();
        Date startDate = new Date(now - 30 * 24 * 60 * 60 * 1000);  // 30 天前
        Date endDate = new Date(now + 30 * 24 * 60 * 60 * 1000);    // 30 天後
        Date outsideDate = new Date(now - 60 * 24 * 60 * 60 * 1000); // 60 天前

        // 準備數據：在範圍內
        IncomeItem itemInRange1 = createIncomeItem(USER_ID, "月薪", 50000);
        itemInRange1.setAccountDate(new Date(now));
        entityManager.persistAndFlush(itemInRange1);

        IncomeItem itemInRange2 = createIncomeItem(USER_ID, "獎金", 20000);
        itemInRange2.setAccountDate(startDate);
        entityManager.persistAndFlush(itemInRange2);

        // 準備數據：超出範圍（太舊）
        IncomeItem itemOutOfRange = createIncomeItem(USER_ID, "舊項目", 5000);
        itemOutOfRange.setAccountDate(outsideDate);
        entityManager.persistAndFlush(itemOutOfRange);

        // 其他用戶的數據
        IncomeItem otherUserItem = createIncomeItem(OTHER_USER_ID, "其他用戶", 10000);
        otherUserItem.setAccountDate(new Date(now));
        entityManager.persistAndFlush(otherUserItem);

        // 執行查詢
        List<IncomeItem> result = repository.findByUserIdAndAccountDateBetween(
                USER_ID, startDate, endDate
        );

        // 驗證：只查到該用戶在日期範圍內的 2 個項目
        assertThat(result)
                .hasSize(3)
                .extracting(IncomeItem::getAccountItem)
                .containsExactlyInAnyOrder("薪");

        assertThat(result)
                .allMatch(item -> item.getUserId().equals(USER_ID));
    }

    @Test
    @DisplayName("日期範圍查詢：開始日期等於結束日期應返回該日期的項目")
    void testFindByUserIdAndAccountDateBetween_SameDateRange() {
        Date targetDate = new Date();

        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        item.setAccountDate(targetDate);
        entityManager.persistAndFlush(item);

        List<IncomeItem> result = repository.findByUserIdAndAccountDateBetween(
                USER_ID, targetDate, targetDate
        );

        assertThat(result)
                .hasSize(1)
                .extracting(IncomeItem::getAccountItem)
                .containsExactly("月薪");
    }

    @Test
    @DisplayName("日期範圍查詢：無符合項目應返回空列表")
    void testFindByUserIdAndAccountDateBetween_NoMatch() {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now - 60 * 24 * 60 * 60 * 1000);
        Date endDate = new Date(now - 30 * 24 * 60 * 60 * 1000);

        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        item.setAccountDate(new Date(now));  // 今天，在範圍外
        entityManager.persistAndFlush(item);

        List<IncomeItem> result = repository.findByUserIdAndAccountDateBetween(
                USER_ID, startDate, endDate
        );

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("日期範圍查詢：不同用戶的項目不應被查到")
    void testFindByUserIdAndAccountDateBetween_DifferentUser() {
        Date now = new Date();

        // User 1 的項目
        IncomeItem user1Item = createIncomeItem(USER_ID, "月薪", 50000);
        user1Item.setAccountDate(now);
        entityManager.persistAndFlush(user1Item);

        // User 2 的項目
        IncomeItem user2Item = createIncomeItem(OTHER_USER_ID, "獎金", 20000);
        user2Item.setAccountDate(now);
        entityManager.persistAndFlush(user2Item);

        // 查詢 User 1 的數據
        List<IncomeItem> result = repository.findByUserIdAndAccountDateBetween(
                USER_ID, now, now
        );

        // 應該只有 User 1 的項目
        assertThat(result)
                .hasSize(1)
                .allMatch(item -> item.getUserId().equals(USER_ID));
    }

    // =============================================
    // 測試 3：findByIncomeItemIdAndUserId
    // =============================================

    @Test
    @DisplayName("應該根據 incomeItemId 和 userId 查詢單個項目")
    void testFindByIncomeItemIdAndUserId_Success() {
        // 準備數據
        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(item);

        Integer incomeItemId = item.getIncomeItemId();

        // 執行查詢
        IncomeItem result = repository.findByIncomeItemIdAndUserId(incomeItemId, USER_ID);

        // 驗證
        assertThat(result)
                .isNotNull()
                .extracting(IncomeItem::getAccountItem)
                .isEqualTo("月薪");

        assertThat(result.getIncomeItemId()).isEqualTo(incomeItemId);
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("不匹配的 userId 應返回 null")
    void testFindByIncomeItemIdAndUserId_WrongUserId() {
        // 準備數據
        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(item);

        Integer incomeItemId = item.getIncomeItemId();

        // 用不同的 userId 查詢
        IncomeItem result = repository.findByIncomeItemIdAndUserId(incomeItemId, OTHER_USER_ID);

        // 驗證
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("不存在的 incomeItemId 應返回 null")
    void testFindByIncomeItemIdAndUserId_NotFound() {
        // 準備數據
        IncomeItem item = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(item);

        // 用不存在的 ID 查詢
        IncomeItem result = repository.findByIncomeItemIdAndUserId(999, USER_ID);

        // 驗證
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("ID 和 userId 都不匹配應返回 null")
    void testFindByIncomeItemIdAndUserId_BothNotMatch() {
        IncomeItem result = repository.findByIncomeItemIdAndUserId(999, 999);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("同一 ID 但不同用戶應返回 null（數據隔離）")
    void testFindByIncomeItemIdAndUserId_DataIsolation() {
        // User 1 的項目
        IncomeItem user1Item = createIncomeItem(USER_ID, "月薪", 50000);
        entityManager.persistAndFlush(user1Item);
        Integer itemId = user1Item.getIncomeItemId();

        // 用不同的 userId 查詢
        IncomeItem result = repository.findByIncomeItemIdAndUserId(itemId, OTHER_USER_ID);

        // 應該返回 null（保護隱私）
        assertThat(result).isNull();
    }

    // =============================================
    // 額外測試：邊界情況和綜合測試
    // =============================================

    @Test
    @DisplayName("多個用戶的數據應被正確隔離")
    void testDataIsolationBetweenUsers() {
        // User 1 的多個項目
        for (int i = 1; i <= 3; i++) {
            IncomeItem item = createIncomeItem(USER_ID, "User1 Item" + i, 10000 * i);
            entityManager.persistAndFlush(item);
        }

        // User 2 的多個項目
        for (int i = 1; i <= 2; i++) {
            IncomeItem item = createIncomeItem(OTHER_USER_ID, "User2 Item" + i, 20000 * i);
            entityManager.persistAndFlush(item);
        }

        // 驗證 User 1 只能查到自己的數據
        List<IncomeItem> user1Results = repository.findByUserId(USER_ID);
        assertThat(user1Results)
                .hasSize(3)
                .allMatch(item -> item.getUserId().equals(USER_ID));

        // 驗證 User 2 只能查到自己的數據
        List<IncomeItem> user2Results = repository.findByUserId(OTHER_USER_ID);
        assertThat(user2Results)
                .hasSize(2)
                .allMatch(item -> item.getUserId().equals(OTHER_USER_ID));
    }

    @Test
    @DisplayName("金額應正確保存和查詢（Integer 類型）")
    void testAmountPreservation() {
        Integer[] amounts = { 1, 50000, 20000, 999999, 100 };

        for (Integer amount : amounts) {
            IncomeItem item = createIncomeItem(USER_ID, "金額：" + amount, amount);
            entityManager.persistAndFlush(item);
        }

        List<IncomeItem> results = repository.findByUserId(USER_ID);

        assertThat(results)
                .hasSize(5)
                .extracting(IncomeItem::getAccountAmount)
                .containsExactlyInAnyOrder(amounts);
    }

    @Test
    @DisplayName("項目名稱應正確保存和查詢")
    void testAccountItemPreservation() {
        String[] items = { "月薪", "獎金", "兼職", "投資收益", "其他" };

        for (String itemName : items) {
            IncomeItem item = createIncomeItem(USER_ID, itemName, 10000);
            entityManager.persistAndFlush(item);
        }

        List<IncomeItem> results = repository.findByUserId(USER_ID);

        assertThat(results)
                .hasSize(5)
                .extracting(IncomeItem::getAccountItem)
                .containsExactlyInAnyOrder(items);
    }

    @Test
    @DisplayName("日期應正確保存和查詢")
    void testAccountDatePreservation() {
        long now = System.currentTimeMillis();

        Date[] dates = {
                new Date(now),
                new Date(now - 24 * 60 * 60 * 1000),
                new Date(now - 7 * 24 * 60 * 60 * 1000),
                new Date(now - 30 * 24 * 60 * 60 * 1000)
        };

        for (Date date : dates) {
            IncomeItem item = createIncomeItem(USER_ID, "項目", 10000);
            item.setAccountDate(date);
            entityManager.persistAndFlush(item);
        }

        List<IncomeItem> results = repository.findByUserId(USER_ID);

        assertThat(results)
                .hasSize(4);

        // 驗證每個項目都有日期
        assertThat(results)
                .allMatch(item -> item.getAccountDate() != null);
    }

    // =============================================
    // 輔助方法
    // =============================================

    /**
     * 創建 IncomeItem 物件的輔助方法
     */
    private IncomeItem createIncomeItem(Integer userId, String accountItem, Integer accountAmount) {
        IncomeItem item = new IncomeItem();
        item.setUserId(userId);
        item.setAccountItem(accountItem);
        item.setAccountAmount(accountAmount);
        item.setAccountDate(new Date());
        return item;
    }
}