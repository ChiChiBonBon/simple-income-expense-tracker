package org.example.simpleincomeexpensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.simpleincomeexpensetracker.entity.ExpenseItem;
import org.example.simpleincomeexpensetracker.entity.IncomeItem;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDataDTO {
    //收入項目清單
    private List<IncomeItem> incomeList;

    //支出項目清單
    private List<ExpenseItem> expenseList;

    //總收入
    private Integer totalIncome;

    //總支出
    private Integer totalExpense;

    //餘額
    private Integer balance;

    //建構子
    public AccountDataDTO(List<IncomeItem> incomeList, List<ExpenseItem> expenseList) {
        this.incomeList = incomeList;
        this.expenseList = expenseList;
    }
}