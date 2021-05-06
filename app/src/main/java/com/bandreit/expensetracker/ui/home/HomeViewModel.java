package com.bandreit.expensetracker.ui.home;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItem;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItemRepository;
import com.bandreit.expensetracker.model.ExpenseItem.SectionOrRow;
import com.bandreit.expensetracker.model.Users.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {

    private final ExpenseItemRepository expenseItemRepository;
    private final UserRepository userRepository;

    public HomeViewModel(Application app) {
        super(app);
        this.expenseItemRepository = ExpenseItemRepository.getInstance();
        this.userRepository = UserRepository.getInstance(app);
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        expenseItemRepository.init(userId);
    }

    public LiveData<List<ExpenseItem>> getGroupedExpenseItems() {
        return expenseItemRepository.getAllExpenseItem();
    }

    public LiveData<List<ExpenseItem>> getExpenseItems() {
        return expenseItemRepository.getAllExpenseItem();
    }
}