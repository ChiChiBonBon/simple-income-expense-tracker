package org.example.simpleincomeexpensetracker.service.impl;

import org.example.simpleincomeexpensetracker.entity.ExpenseItem;
import org.example.simpleincomeexpensetracker.repository.ExpenseItemRepository;
import org.example.simpleincomeexpensetracker.service.ExpenseItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public ExpenseItem findByExpenseItemIdAndUserId(Integer expenseItemId, Integer userId) {
        return expenseItemRepository.findByExpenseItemIdAndUserId(expenseItemId,userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end) {
        return expenseItemRepository.findByUserIdAndAccountDateBetween(userId,start,end);
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
    public void deleteByExpenseItemId(Integer id) {
        log.info("删除支出項目，ID: {}", id);
        expenseItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseItem> findByUserId(Integer userId) {
        return expenseItemRepository.findByUserId(userId);
    }
}