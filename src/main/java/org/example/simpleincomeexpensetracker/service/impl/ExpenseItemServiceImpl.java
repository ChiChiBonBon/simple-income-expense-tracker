package org.example.simpleincomeexpensetracker.service.impl;

import org.example.simpleincomeexpensetracker.entity.ExpenseItem;
import org.example.simpleincomeexpensetracker.repository.ExpenseItemRepository;
import org.example.simpleincomeexpensetracker.service.ExpenseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ExpenseItemServiceImpl implements ExpenseItemService {

    private final static Logger log = LoggerFactory.getLogger(ExpenseItemServiceImpl.class);
    private final ExpenseItemRepository expenseItemRepository;

    public ExpenseItemServiceImpl(ExpenseItemRepository expenseItemRepository) {
        this.expenseItemRepository = expenseItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseItem> findByExpenseItemList() {
        log.info("查詢所有支出項目");
        List<ExpenseItem> list = expenseItemRepository.findAll();
        log.info("查詢到 {} 筆支出記錄", list.size());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseItem findById(Long id) {
        log.info("查詢支出項目，ID: {}", id);
        return expenseItemRepository.findById(id).orElse(null);
    }

    @Override
    public ExpenseItem save(ExpenseItem expenseItem) {
        log.info("保存支出項目: {}", expenseItem);
        return expenseItemRepository.save(expenseItem);
    }

    @Override
    public ExpenseItem update(ExpenseItem expenseItem) {
        log.info("更新支出項目: {}", expenseItem);
        return expenseItemRepository.save(expenseItem);
    }

    @Override
    public void deleteById(Long id) {
        log.info("删除支出項目，ID: {}", id);
        expenseItemRepository.deleteById(id);
    }

    public List<ExpenseItem> findByDateRange(Date startDate, Date endDate) {
        return expenseItemRepository.findByAccountDateBetween(startDate, endDate);
    }
}