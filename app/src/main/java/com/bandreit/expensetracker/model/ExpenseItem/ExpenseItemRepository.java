package com.bandreit.expensetracker.model.ExpenseItem;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseItemRepository {
    private ExpenseItemDao expenseItemDao;
    private static ExpenseItemRepository instance;

    private ExpenseItemRepository() {
        expenseItemDao = ExpenseItemDao.getInstance();
    }

    public static synchronized ExpenseItemRepository getInstance() {
        if (instance == null)
            instance = new ExpenseItemRepository();
        return instance;
    }

    public LiveData<List<ExpenseItem>> getAllExpenseItem() {
        return expenseItemDao.getAllExpenseItems();
    }

    public void addExpenseItem(ExpenseItem expenseItem) {
        expenseItemDao.addExpenseItem(expenseItem);
    }

    public void deleteExpense(String id) {
        expenseItemDao.deleteExpense(id);
    }

    public void init(String userId) {
        expenseItemDao.init(userId);
    }
}
