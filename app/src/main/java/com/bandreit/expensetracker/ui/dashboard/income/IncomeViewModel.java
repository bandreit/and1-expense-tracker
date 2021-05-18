package com.bandreit.expensetracker.ui.dashboard.income;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistory;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.bandreit.expensetracker.model.users.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncomeViewModel extends AndroidViewModel {
    private final TransactionItemRepository transactionItemRepository;
    private final UserRepository userRepository;
    public MutableLiveData<Integer> selectedYear;

    public IncomeViewModel(Application app) {
        super(app);
        this.transactionItemRepository = TransactionItemRepository.getInstance();
        this.userRepository = UserRepository.getInstance(app);
        selectedYear = new MutableLiveData<>();
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        transactionItemRepository.init(userId);
    }

    public LiveData<List<TransactionItem>> getAllExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }

    public MutableLiveData<Integer> getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer year) {
        selectedYear.setValue(year);
    }

    public ArrayList<ExpenseHistory> filterTransactionItemsByCategoryAndDate(List<TransactionItem> transactionItems) {
        ArrayList<ExpenseHistory> expenseHistoryArrayList = new ArrayList<>();

        // Map of Expense Items by Category
        Map<String, List<TransactionItem>> categorisedExpenseItemsMap = new HashMap<>();

        if (transactionItems != null) {
            for (TransactionItem eItem : transactionItems) {
                String key = eItem.getCategory().getName();
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

        List<String> categoriesToSortBy = new ArrayList<>(categorisedExpenseItemsMap.keySet());

        for (String category : categoriesToSortBy) {
            List<TransactionItem> itemsByCategory = categorisedExpenseItemsMap.get(category);
            // Map of Expense Items By Date for each Category
            Map<Calendar, List<TransactionItem>> expenseItemsByDateMap = new HashMap<>();
            for (TransactionItem transactionItem : itemsByCategory) {
                Calendar key = new GregorianCalendar(transactionItem.getDate().get(Calendar.YEAR), transactionItem.getDate().get(Calendar.MONTH), 1);

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
            Collections.sort(datesToSortBy);
            Collections.reverse(datesToSortBy);

            for (Calendar date : datesToSortBy) {
                List<TransactionItem> itemsByDate = expenseItemsByDateMap.get(date);

                double sum = 0;
                for (TransactionItem transactionItem : itemsByDate) {
                    sum += transactionItem.getAmount().getCurrencyAmount();
                }
                expenseHistoryArrayList.add(new ExpenseHistory(itemsByDate.get(0).getCategory(), date, new TransactionAmount("DKK", sum), TransactionType.EXPENSE));
            }

        }

        return expenseHistoryArrayList;
    }


    public Map<Integer, List<ExpenseHistory>> filterTransactionItemsByMonth(ArrayList<ExpenseHistory> expenseHistoryArrayList) {
        Map<Integer, List<ExpenseHistory>> categorisedExpenseItemsMap = new HashMap<>();

        if (expenseHistoryArrayList != null) {
            for (ExpenseHistory eItem : expenseHistoryArrayList) {
                int key = eItem.getDate().get(Calendar.MONTH);
                if (categorisedExpenseItemsMap.containsKey(key)) {
                    List<ExpenseHistory> list = categorisedExpenseItemsMap.get(key);
                    list.add(eItem);
                } else {
                    List<ExpenseHistory> list = new ArrayList<>();
                    list.add(eItem);
                    categorisedExpenseItemsMap.put(key, list);
                }
            }
        }
        return categorisedExpenseItemsMap;
    }
}