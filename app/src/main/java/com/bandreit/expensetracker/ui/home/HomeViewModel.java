package com.bandreit.expensetracker.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.users.UserRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final TransactionItemRepository transactionItemRepository;
    private final UserRepository userRepository;

    public HomeViewModel(Application app) {
        super(app);
        this.transactionItemRepository = TransactionItemRepository.getInstance();
        this.userRepository = UserRepository.getInstance(app);
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        transactionItemRepository.init(userId);
    }

    public LiveData<List<TransactionItem>> getGroupedExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }

    public LiveData<List<TransactionItem>> getExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }
}