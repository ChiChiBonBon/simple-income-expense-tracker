package org.example.simpleincomeexpensetracker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expense_item")
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_item_id")
    private Long expenseItemId;

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

    public Long getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(Long expenseItemId) {
        this.expenseItemId = expenseItemId;
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