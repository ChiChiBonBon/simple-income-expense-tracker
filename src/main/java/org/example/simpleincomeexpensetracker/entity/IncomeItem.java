package org.example.simpleincomeexpensetracker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "income_item")
public class IncomeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "income_item_id")
    private Long incomeItemId;

    @Column(name = "account_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date accountDate;

    @Column(name = "account_item")
    private String accountItem;

    @Column(name = "account_amount")
    private Integer accountAmount;

    @Column(name = "user_id")
    private Integer userId;

    public Long getIncomeItemId() {
        return incomeItemId;
    }

    public void setIncomeItemId(Long incomeItemId) {
        this.incomeItemId = incomeItemId;
    }

    public Date getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(Date accountDate) {
        this.accountDate = accountDate;
    }

    public String getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(String accountItem) {
        this.accountItem = accountItem;
    }

    public Integer getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Integer accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}