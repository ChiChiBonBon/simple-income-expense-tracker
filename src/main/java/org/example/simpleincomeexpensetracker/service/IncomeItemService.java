package org.example.simpleincomeexpensetracker.service;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;

import java.util.Date;
import java.util.List;

public interface IncomeItemService {
    IncomeItem save(IncomeItem incomeItem);
    IncomeItem update(IncomeItem incomeItem);
    void deleteByIncomeItemId(Integer id);
    List<IncomeItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end);
    List<IncomeItem> findByUserId(Integer userId);
    IncomeItem findByIncomeItemIdAndUserId(Integer incomeItemId,Integer userId);
}