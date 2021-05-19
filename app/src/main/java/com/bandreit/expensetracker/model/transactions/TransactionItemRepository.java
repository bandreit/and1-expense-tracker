package com.bandreit.expensetracker.model.transactions;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TransactionItemRepository {
    private TransactionItemDao transactionItemDao;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static TransactionItemRepository instance;

    private TransactionItemRepository() {
        transactionItemDao = TransactionItemDao.getInstance();
    }

    public void init(String userId) {
        transactionItemDao.init(userId);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteExpense(String id) {
        transactionItemDao.deleteExpense(id);
    }

    public void editExpenseItem(TransactionItem value, TransactionAmount currentAmount) {
        transactionItemDao.editExpenseItem(value, currentAmount);
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
