package com.bandreit.expensetracker.model.transactions;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bandreit.expensetracker.model.balance.Balance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.function.Consumer;

public class TransactionItemDao {
    private TransactionItemLiveData allExpenseItems;
    private static TransactionItemDao instance;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference expenseItemsRef;
    private DatabaseReference balanceRef;

    private TransactionItemDao() {
    }

    public static synchronized TransactionItemDao getInstance() {
        if (instance == null) {
            instance = new TransactionItemDao();
        }
        return instance;
    }

    public void init(String userId) {
        this.database = FirebaseDatabase.getInstance("https://and1-expensetracker-default-rtdb.europe-west1.firebasedatabase.app/");
        this.userRef = database.getReference().child("users").child(userId);
        this.expenseItemsRef = userRef.child("expense_items");
        this.balanceRef = userRef.child("balance");
        this.allExpenseItems = new TransactionItemLiveData(expenseItemsRef);
    }

    public TransactionItemLiveData getAllExpenseItems() {
        return allExpenseItems;
    }

    public void addExpenseItem(TransactionItem transactionItem) {
        expenseItemsRef.push().setValue(transactionItem);

        balanceRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Balance currentBalance = dataSnapshot.getChildren().iterator().next().getValue(Balance.class);
                double currentBalanceAmount = 0;
                if (currentBalance == null) {
                    balanceRef.push().setValue(new Balance(new TransactionAmount("DKK", 0), Calendar.getInstance().getTimeInMillis()));
                } else {
                    currentBalanceAmount = currentBalance.getTransactionAmount().getCurrencyAmount();
                }

                int sign = transactionItem.getType() == TransactionType.EXPENSE ? -1 : 1;

                double newBalanceAmount = currentBalanceAmount + sign * transactionItem.getAmount().getCurrencyAmount();
                Balance newBalance = new Balance(new TransactionAmount("DKK", newBalanceAmount), Calendar.getInstance().getTimeInMillis());
                balanceRef.push().setValue(newBalance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error:", "Error retrieving data");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteExpense(String id) {
        Optional<TransactionItem> transactionItemToDelete = allExpenseItems.getValue().stream().filter(eItem -> eItem.getId().equals(id)).findFirst();

        transactionItemToDelete.ifPresent(new Consumer<TransactionItem>() {
            @Override
            public void accept(TransactionItem transactionItem) {
                double amountOfDeletedItem = transactionItem.getAmount().getCurrencyAmount();
                expenseItemsRef.child(id).removeValue();

                balanceRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Balance currentBalance = dataSnapshot.getChildren().iterator().next().getValue(Balance.class);
                        double currentBalanceAmount = 0;
                        if (currentBalance == null) {
                            balanceRef.push().setValue(new Balance(new TransactionAmount("DKK", 0), Calendar.getInstance().getTimeInMillis()));
                        } else {
                            currentBalanceAmount = currentBalance.getTransactionAmount().getCurrencyAmount();
                        }

                        int sign = transactionItem.getType() == TransactionType.EXPENSE ? 1 : -1;

                        double newBalanceAmount = currentBalanceAmount + sign * amountOfDeletedItem;
                        Balance newBalance = new Balance(new TransactionAmount("DKK", newBalanceAmount), Calendar.getInstance().getTimeInMillis());
                        balanceRef.push().setValue(newBalance);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Error:", "Error retrieving data");
                    }
                });

            }
        });
    }
}
