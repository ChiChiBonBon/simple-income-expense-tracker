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
public interface IncomeItemRepository extends JpaRepository<IncomeItem, Integer> {
    List<IncomeItem> findByUserId(Integer userId);
    List<IncomeItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end);
    IncomeItem findByIncomeItemIdAndUserId(Integer incomeItemId, Integer userId);
}