package com.bandreit.expensetracker.ui.edit.editItem;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.categories.CategoryRepository;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class EditItemViewModel extends ViewModel {

    private final CategoryRepository categoryRepository;
    private TransactionItemRepository transactionItemRepository;
    private TransactionItem localTransactionItem;
    private MutableLiveData<TransactionItem> expenseItemToAdd;
    private static EditItemViewModel instance;

    public static synchronized EditItemViewModel getInstance() {
        if (instance == null)
            instance = new EditItemViewModel();
        return instance;
    }

    private EditItemViewModel() {
        categoryRepository = new CategoryRepository();
        transactionItemRepository = TransactionItemRepository.getInstance();
        localTransactionItem = new TransactionItem();
        expenseItemToAdd = new MutableLiveData<>();
    }

    public LiveData<TransactionItem> getCurrentExpenseItem() {
        return expenseItemToAdd;
    }


    public void selectCategory(Category clickedCategory) {
        localTransactionItem = expenseItemToAdd.getValue();
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

    public void setDownloadUri(Uri downloadUri) {
        if (downloadUri == null) localTransactionItem.setImageUri("");
        else
            localTransactionItem.setImageUri(downloadUri.toString());
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

    public StorageReference getStorageReference() {
        return transactionItemRepository.getStorageReference();
    }

    public void editLocalExpenseItem(TransactionItem transactionItem) {
        expenseItemToAdd.setValue(transactionItem);
    }

    public void editItem(TransactionAmount currentAmount) {
        transactionItemRepository.editExpenseItem(expenseItemToAdd.getValue(), currentAmount);
    }

}