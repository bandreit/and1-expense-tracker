package com.bandreit.expensetracker.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bandreit.expensetracker.model.balance.Balance;
import com.bandreit.expensetracker.model.balance.BalanceRepository;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.users.UserRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final TransactionItemRepository transactionItemRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    public HomeViewModel(Application app) {
        super(app);
        this.userRepository = UserRepository.getInstance(app);
        this.transactionItemRepository = TransactionItemRepository.getInstance();
        this.balanceRepository = BalanceRepository.getInstance();
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        transactionItemRepository.init(userId);
        balanceRepository.init(userId);
    }

    public LiveData<List<TransactionItem>> getGroupedExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }

    public LiveData<List<Balance>> getBalanceHistory() {
        return balanceRepository.getAllBalanceHistory();
    }
}