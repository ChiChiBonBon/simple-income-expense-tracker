package org.example.simpleincomeexpensetracker.service.impl;

import org.example.simpleincomeexpensetracker.entity.IncomeItem;
import org.example.simpleincomeexpensetracker.repository.IncomeItemRepository;
import org.example.simpleincomeexpensetracker.service.IncomeItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class IncomeItemServiceImpl implements IncomeItemService {

    private final static Logger log = LoggerFactory.getLogger(IncomeItemServiceImpl.class);
    private final IncomeItemRepository incomeItemRepository;

    public IncomeItemServiceImpl(IncomeItemRepository incomeItemRepository) {
        this.incomeItemRepository = incomeItemRepository;
    }

    @Override
    public IncomeItem save(IncomeItem incomeItem) {
        log.info("保存收入项目: {}", incomeItem);
        return incomeItemRepository.save(incomeItem);
    }

    @Override
    public IncomeItem update(IncomeItem incomeItem) {
        log.info("更新收入项目: {}", incomeItem);
        return incomeItemRepository.save(incomeItem);
    }

    @Override
    public void deleteByIncomeItemId(Integer incomeItemId) {
        incomeItemRepository.deleteById(incomeItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeItem> findByUserIdAndAccountDateBetween(Integer userId, Date start, Date end) {
        return incomeItemRepository.findByUserIdAndAccountDateBetween(userId, start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeItem> findByUserId(Integer userId) {
        return incomeItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeItem findByIncomeItemIdAndUserId(Integer incomeItemId, Integer userId) {
        return incomeItemRepository.findByIncomeItemIdAndUserId(incomeItemId, userId);
    }
}