package com.bandreit.expensetracker.model.ExpenseItem;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.Categories.Category;
import com.bandreit.expensetracker.model.Users.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseItemDao {
    private ExpenseItemLiveData allExpenseItems;
    private static ExpenseItemDao instance;
    private FirebaseDatabase database;
    private DatabaseReference expenseItemsRef;

    private ExpenseItemDao() {
    }

    public static synchronized ExpenseItemDao getInstance() {
        if (instance == null) {
            instance = new ExpenseItemDao();
        }
        return instance;
    }

    public ExpenseItemLiveData getAllExpenseItems() {
        return allExpenseItems;
    }

    public void addExpenseItem(ExpenseItem expenseItem) {
        expenseItemsRef.push().setValue(expenseItem);
    }

    public void init(String userId) {
        this.database = FirebaseDatabase.getInstance("https://and1-expensetracker-default-rtdb.europe-west1.firebasedatabase.app/");
        this.expenseItemsRef = database.getReference().child("users").child(userId).child("expense_items");
        this.allExpenseItems = new ExpenseItemLiveData(expenseItemsRef);

//        ArrayList<ExpenseItem> expenseItems = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MONTH, 3);
//        calendar.set(Calendar.DATE, 30);
//        expenseItems.add(new ExpenseItem("Apple store", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi), calendar, new ExpenseAmount("DKK", 933), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("Wise", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg2, R.drawable.ic_asvigi), calendar, new ExpenseAmount("DKK", 5100), ExpenseType.EXPENSE));
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(Calendar.MONTH, 3);
//        calendar2.set(Calendar.DATE, 29);
//        expenseItems.add(new ExpenseItem("Uber", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg5, R.drawable.ic_asvigi), calendar2, new ExpenseAmount("DKK", 43), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("McDonald's", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg3, R.drawable.ic_asvigi), calendar2, new ExpenseAmount("DKK", 43.50), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("Starbucks", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg2, R.drawable.ic_asvigi), calendar2, new ExpenseAmount("DKK", 250), ExpenseType.EXPENSE));
//        Calendar calendar3 = Calendar.getInstance();
//        calendar3.set(Calendar.MONTH, 2);
//        expenseItems.add(new ExpenseItem("DoorDash", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg4, R.drawable.ic_asvigi), calendar3, new ExpenseAmount("DKK", 343), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("Lidl", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg2, R.drawable.ic_asvigi), calendar3, new ExpenseAmount("DKK", 69), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("Netto", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi), calendar3, new ExpenseAmount("DKK", 25.50), ExpenseType.EXPENSE));
//        Calendar calendar4 = Calendar.getInstance();
//        calendar4.set(Calendar.YEAR, 2020);
//        expenseItems.add(new ExpenseItem("Bilka", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg5, R.drawable.ic_asvigi), calendar4, new ExpenseAmount("DKK", 120), ExpenseType.EXPENSE));
//        expenseItems.add(new ExpenseItem("Netto", new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg2, R.drawable.ic_asvigi), calendar4, new ExpenseAmount("DKK", 500), ExpenseType.EXPENSE));
//
//        for (ExpenseItem item : expenseItems) {
//            expenseItemsRef.push().setValue(item);
//        }
    }

    public void deleteExpense(String id) {
        expenseItemsRef.child(id).removeValue();
    }
}
