package com.bandreit.expensetracker.ui.addItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.Categories.Category;
import com.bandreit.expensetracker.model.Categories.CategoryRepository;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseAmount;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItem;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItemRepository;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseType;

import java.util.Calendar;
import java.util.List;

public class AddItemViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private ExpenseItemRepository expenseItemRepository;
    private final ExpenseItem localExpenseItem;
    private final MutableLiveData<ExpenseItem> expenseItemToAdd;
    private static AddItemViewModel instance;

    public static synchronized AddItemViewModel getInstance() {
        if (instance == null)
            instance = new AddItemViewModel();
        return instance;
    }

    private AddItemViewModel() {
        categoryRepository = new CategoryRepository();
        expenseItemRepository = ExpenseItemRepository.getInstance();
        localExpenseItem = new ExpenseItem();
        expenseItemToAdd = new MutableLiveData<>();
    }

    public void addItem() {
        expenseItemRepository.addExpenseItem(expenseItemToAdd.getValue());
    }

    public LiveData<ExpenseItem> getCurrentExpenseItem() {
        return expenseItemToAdd;
    }

    public void selectCategory(Category clickedCategory) {
        localExpenseItem.setCategory(clickedCategory);
        expenseItemToAdd.setValue(localExpenseItem);
    }

    public void selectExpenseType(ExpenseType expenseType) {
        localExpenseItem.setType(expenseType);
        expenseItemToAdd.setValue(localExpenseItem);
    }

    public void selectAmount(ExpenseAmount expenseAmount) {
        localExpenseItem.setAmount(expenseAmount);
        expenseItemToAdd.setValue(localExpenseItem);
    }

    public void selectTitle(String title) {
        localExpenseItem.setTitle(title);
        expenseItemToAdd.setValue(localExpenseItem);
    }

    public LiveData<List<Category>> getCategories() {
        return categoryRepository.getOwnCategories();
    }

    public void selectDate(int selectedDay, int selectedMonth, int selectedYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay);
        localExpenseItem.setTimestamp(calendar.getTimeInMillis());
        localExpenseItem.setDate(calendar);
        expenseItemToAdd.setValue(localExpenseItem);
    }
}