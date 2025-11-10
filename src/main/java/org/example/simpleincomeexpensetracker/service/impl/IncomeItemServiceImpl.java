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
    @Transactional(readOnly = true)
    public List<IncomeItem> findByIncomeItemList() {
        log.info("查詢所有收入項目");
        List<IncomeItem> list = incomeItemRepository.findAll();
        log.info("查詢到 {} 筆收入記錄", list.size());
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeItem findById(Long id) {
        log.info("查询收入项目，ID: {}", id);
        return incomeItemRepository.findById(id).orElse(null);
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
    public void deleteById(Long id) {
        log.info("删除收入项目，ID: {}", id);
        incomeItemRepository.deleteById(id);
    }

    public List<IncomeItem> findByDateRange(Date startDate, Date endDate) {
        return incomeItemRepository.findByAccountDateBetween(startDate, endDate);
    }
}