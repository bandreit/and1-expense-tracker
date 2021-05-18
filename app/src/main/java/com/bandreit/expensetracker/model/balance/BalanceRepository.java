package com.bandreit.expensetracker.model.balance;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemDao;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BalanceRepository {
    private BalanceLiveData balanceLiveData;
    private static BalanceRepository instance;
    private DatabaseReference balanceRef;


    private BalanceRepository() {
    }

    public static synchronized BalanceRepository getInstance() {
        if (instance == null)
            instance = new BalanceRepository();
        return instance;
    }

    public void init(String userId) {
        this.balanceRef = FirebaseDatabase.getInstance("https://and1-expensetracker-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users").child(userId).child("balance");
        balanceLiveData = new BalanceLiveData(balanceRef);
    }

    public LiveData<List<Balance>> getAllBalanceHistory() {
        return balanceLiveData;
    }

}