package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;

import java.util.Date;
import java.util.List;

public interface IncomeItemService {
    List<IncomeItem> findByIncomeItemList();
    IncomeItem findById(Long id);
    IncomeItem save(IncomeItem incomeItem);
    IncomeItem update(IncomeItem incomeItem);
    void deleteById(Long id);
    List<IncomeItem> findByDateRange(Date startDate, Date endDate);
}