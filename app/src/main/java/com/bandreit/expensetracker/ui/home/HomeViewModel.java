package com.bandreit.expensetracker.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItemRepository;
import com.bandreit.expensetracker.model.ExpenseItem.SectionOrRow;

import java.util.List;

public class HomeViewModel extends ViewModel {

    ExpenseItemRepository repository = new ExpenseItemRepository();

    public LiveData<List<SectionOrRow>> getExpenseItems() {
        return repository.getSearchedExpenseItems();
    }
}