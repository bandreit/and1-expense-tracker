package com.bandreit.expensetracker.model.ExpenseItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bandreit.expensetracker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseItemRepository {
    private final List<SectionOrRow> expenseItems;
    private final MutableLiveData<List<SectionOrRow>> searchedExpenseItems = new MutableLiveData<>();

    public ExpenseItemRepository() {
        this.expenseItems = new ArrayList<>();
        expenseItems.add(SectionOrRow.createSection("03 April, 2021"));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Apple store", "Technology", new Date(), new ExpenseAmount("DKK",933), ExpenseType.EXPENSE, R.drawable.ic_asvigi)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Wise", "Salary & Payment", new Date(), new ExpenseAmount("DKK",5100), ExpenseType.EXPENSE, R.drawable.ic_baseline_emoji_objects_24)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Uber", "Ride & Food", new Date(), new ExpenseAmount("DKK",43), ExpenseType.EXPENSE, R.drawable.ic_baseline_emoji_objects_24)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("McDonald's", "Foods & Coffee", new Date(), new ExpenseAmount("DKK",43.50), ExpenseType.EXPENSE, R.drawable.ic_asvigi)));
        expenseItems.add(SectionOrRow.createSection("04 April, 2021"));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Starbucks", "Restaurant", new Date(), new ExpenseAmount("DKK",250), ExpenseType.EXPENSE, R.drawable.ic_baseline_emoji_objects_24)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("DoorDash", "Rental", new Date(), new ExpenseAmount("DKK",343), ExpenseType.EXPENSE, R.drawable.ic_asvigi)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Lidl", "Groceries", new Date(), new ExpenseAmount("DKK",69), ExpenseType.EXPENSE, R.drawable.ic_baseline_emoji_objects_24)));
        expenseItems.add(SectionOrRow.createSection("03 April, 2021"));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Netto", "Groceries", new Date(), new ExpenseAmount("DKK",25.50), ExpenseType.EXPENSE, R.drawable.ic_asvigi)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Bilka", "Groceries", new Date(), new ExpenseAmount("DKK",120), ExpenseType.EXPENSE, R.drawable.ic_asvigi)));
        expenseItems.add(SectionOrRow.createRow(new ExpenseItem("Netto", "Groceries", new Date(), new ExpenseAmount("DKK",500), ExpenseType.EXPENSE, R.drawable.ic_baseline_emoji_objects_24)));
        searchedExpenseItems.setValue(expenseItems);
    }

    public LiveData<List<SectionOrRow>> getSearchedExpenseItems() {return searchedExpenseItems;}
}
