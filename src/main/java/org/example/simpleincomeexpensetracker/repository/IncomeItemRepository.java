package org.example.simpleincomeexpensetracker.repository;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 收入項目資料存取層介面
 * 繼承 JpaRepository 提供基本的 CRUD 操作
 */
@Repository  // 標註為 Spring 的資料存取層元件，由 Spring 容器管理
public interface IncomeItemRepository extends JpaRepository<IncomeItem, Long> {
    // 繼承 JpaRepository<IncomeItem, Long>：
    // - IncomeItem：實體類別
    // - Long：主鍵類型
    // 自動提供 save(), findById(), findAll(), deleteById() 等基本方法

    /**
     * 根據日期範圍查詢收入項目
     * 使用 Spring Data JPA 的方法命名規則自動生成查詢語句
     *
     * 方法名稱解析：
     * - findBy：查詢前綴
     * - AccountDate：實體類別中的欄位名稱
     * - Between：SQL 的 BETWEEN 條件
     *
     * 等同 SQL：SELECT * FROM income_item WHERE account_date BETWEEN ? AND ?
     *
     */
    List<IncomeItem> findByAccountDateBetween(Date startDate, Date endDate);

    List<IncomeItem> findByUserId(Integer userId);
    List<IncomeItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end);
}