package com.bandreit.expensetracker.model.transactions;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TransactionItemRepository {
    private TransactionItemDao transactionItemDao;
    private static TransactionItemRepository instance;

    private TransactionItemRepository() {
        transactionItemDao = TransactionItemDao.getInstance();
    }

    public static synchronized TransactionItemRepository getInstance() {
        if (instance == null)
            instance = new TransactionItemRepository();
        return instance;
    }

    public LiveData<List<TransactionItem>> getAllExpenseItem() {
        return transactionItemDao.getAllExpenseItems();
    }

    public void addExpenseItem(TransactionItem transactionItem) {
        transactionItemDao.addExpenseItem(transactionItem);
    }

    public void deleteExpense(String id) {
        transactionItemDao.deleteExpense(id);
    }

    public void init(String userId) {
        transactionItemDao.init(userId);
    }
}
