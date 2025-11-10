package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.entity.ExpenseItem;

import java.util.Date;
import java.util.List;

public interface ExpenseItemService {
    List<ExpenseItem> findByExpenseItemList();
    ExpenseItem findById(Long id);
    ExpenseItem save(ExpenseItem expenseItem);
    ExpenseItem update(ExpenseItem expenseItem);
    void deleteById(Long id);
    List<ExpenseItem> findByDateRange(Date startDate, Date endDate);
}