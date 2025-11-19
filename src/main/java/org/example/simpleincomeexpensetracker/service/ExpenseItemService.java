package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.entity.ExpenseItem;

import java.util.Date;
import java.util.List;

public interface ExpenseItemService {
    ExpenseItem save(ExpenseItem expenseItem);
    ExpenseItem update(ExpenseItem expenseItem);
    void deleteByExpenseItemId(Integer id);
    List<ExpenseItem> findByUserId(Integer userId);
    ExpenseItem findByExpenseItemIdAndUserId(Integer expenseItemId, Integer userId);
    List<ExpenseItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end);
}