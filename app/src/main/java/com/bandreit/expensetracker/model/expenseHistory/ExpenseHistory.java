package com.bandreit.expensetracker.model.expenseHistory;

import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseHistory {
    private String id;
    private Category category;
    private Calendar date;
    private TransactionAmount amount;
    private TransactionType type;

    public ExpenseHistory(Category category, Calendar date, TransactionAmount amount, TransactionType type) {
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public ExpenseHistory() {
    }

    public static ArrayList<ExpenseHistory> fromExpenseItemsToExpenseHistoryList(List<TransactionItem> transactionItems) {
        ArrayList<ExpenseHistory> expenseHistoryArrayList = new ArrayList<>();
        for (TransactionItem transactionItem : transactionItems) {
            expenseHistoryArrayList.add(new ExpenseHistory(transactionItem.getCategory(), transactionItem.getDate(), transactionItem.getAmount(), transactionItem.getType()));
        }
        return expenseHistoryArrayList;
    }

    public static ExpenseHistory fromExpenseItemToExpenseHistory(TransactionItem transactionItem) {
        return new ExpenseHistory(transactionItem.getCategory(), transactionItem.getDate(), transactionItem.getAmount(), transactionItem.getType());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public TransactionAmount getAmount() {
        return amount;
    }

    public void setAmount(TransactionAmount amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
