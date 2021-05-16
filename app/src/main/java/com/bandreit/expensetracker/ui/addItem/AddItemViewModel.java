package com.bandreit.expensetracker.ui.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.categories.CategoryRepository;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.transactions.TransactionType;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AddItemViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private TransactionItemRepository transactionItemRepository;
    private final TransactionItem localTransactionItem;
    private final MutableLiveData<TransactionItem> expenseItemToAdd;
    private static AddItemViewModel instance;

    public static synchronized AddItemViewModel getInstance() {
        if (instance == null)
            instance = new AddItemViewModel();
        return instance;
    }

    private AddItemViewModel() {
        categoryRepository = new CategoryRepository();
        transactionItemRepository = TransactionItemRepository.getInstance();
        localTransactionItem = new TransactionItem();
        expenseItemToAdd = new MutableLiveData<>();
    }

    public void addItem() {
        transactionItemRepository.addExpenseItem(expenseItemToAdd.getValue());
    }

    public LiveData<TransactionItem> getCurrentExpenseItem() {
        return expenseItemToAdd;
    }

    public void selectCategory(Category clickedCategory) {
        localTransactionItem.setCategory(clickedCategory);
        expenseItemToAdd.setValue(localTransactionItem);
    }

    public void selectExpenseType(TransactionType transactionType) {
        localTransactionItem.setType(transactionType);
        expenseItemToAdd.setValue(localTransactionItem);
    }

    public void selectAmount(TransactionAmount transactionAmount) {
        localTransactionItem.setAmount(transactionAmount);
        expenseItemToAdd.setValue(localTransactionItem);
    }

    public void selectTitle(String title) {
        localTransactionItem.setTitle(title);
        expenseItemToAdd.setValue(localTransactionItem);
    }

    public LiveData<List<Category>> getCategories() {
        return categoryRepository.getOwnCategories();
    }

    public void selectDate(int selectedDay, int selectedMonth, int selectedYear) {
        Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
        localTransactionItem.setTimestamp(calendar.getTimeInMillis());
        localTransactionItem.setDate(calendar);
        expenseItemToAdd.setValue(localTransactionItem);
    }
}