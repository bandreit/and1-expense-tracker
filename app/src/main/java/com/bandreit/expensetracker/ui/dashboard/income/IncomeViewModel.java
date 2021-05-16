package com.bandreit.expensetracker.ui.dashboard.income;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistory;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.bandreit.expensetracker.model.users.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeViewModel extends AndroidViewModel {
    private final TransactionItemRepository transactionItemRepository;
    private final UserRepository userRepository;

    public IncomeViewModel(Application app) {
        super(app);
        this.transactionItemRepository = TransactionItemRepository.getInstance();
        this.userRepository = UserRepository.getInstance(app);
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        transactionItemRepository.init(userId);
    }

    public LiveData<List<TransactionItem>> getAllExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }

    public ArrayList<ExpenseHistory> filterExpenseItemsByCategory(List<TransactionItem> transactionItems) {
        ArrayList<ExpenseHistory> expenseHistoryArrayList = new ArrayList<>();

        // Map of Expense Items by Category
        Map<Category, List<TransactionItem>> categorisedExpenseItemsMap = new HashMap<>();

        if (transactionItems != null) {
            for (TransactionItem eItem : transactionItems) {
                Category key = eItem.getCategory();
                if (categorisedExpenseItemsMap.containsKey(key)) {
                    List<TransactionItem> list = categorisedExpenseItemsMap.get(key);
                    list.add(eItem);
                } else {
                    List<TransactionItem> list = new ArrayList<TransactionItem>();
                    list.add(eItem);
                    categorisedExpenseItemsMap.put(key, list);
                }
            }
        }

        List<Category> categoriesToSortBy = new ArrayList<>(categorisedExpenseItemsMap.keySet());

        for (Category category : categoriesToSortBy) {
            List<TransactionItem> itemsByCategory = categorisedExpenseItemsMap.get(category);
            // Map of Expense Items By Date for each Category
            Map<Calendar, List<TransactionItem>> expenseItemsByDateMap = new HashMap<>();
            for (TransactionItem transactionItem : itemsByCategory) {
                Calendar key = transactionItem.getDate();

                key.set(Calendar.HOUR_OF_DAY, 0);
                key.set(Calendar.MINUTE, 0);
                key.set(Calendar.SECOND, 0);
                key.set(Calendar.MILLISECOND, 0);
                key.set(Calendar.DATE, 0);

                if (expenseItemsByDateMap.containsKey(key)) {
                    List<TransactionItem> list = expenseItemsByDateMap.get(key);
                    list.add(transactionItem);
                } else {
                    List<TransactionItem> list = new ArrayList<TransactionItem>();
                    list.add(transactionItem);
                    expenseItemsByDateMap.put(key, list);
                }
            }

            List<Calendar> datesToSortBy = new ArrayList<>(expenseItemsByDateMap.keySet());

            for (Calendar date : datesToSortBy) {
                List<TransactionItem> itemsByDate = categorisedExpenseItemsMap.get(category);

                double sum = 0;
                for (TransactionItem transactionItem : itemsByDate) {
                    sum += transactionItem.getAmount().getCurrencyAmount();
                }
                expenseHistoryArrayList.add(new ExpenseHistory(category, date, new TransactionAmount("DKK", sum), TransactionType.INCOME));
            }

        }

        return expenseHistoryArrayList;
    }
}