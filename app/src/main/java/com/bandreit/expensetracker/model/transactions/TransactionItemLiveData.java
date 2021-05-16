package com.bandreit.expensetracker.model.transactions;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class TransactionItemLiveData extends LiveData<List<TransactionItem>> {
    private final ArrayList<TransactionItem> localTransactionItems;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            localTransactionItems.clear();
            for (DataSnapshot eItemSnapshot : snapshot.getChildren()) {

                TransactionItem retrievedTransactionItem = eItemSnapshot.getValue(TransactionItem.class);
                Calendar dateFromTimestamp = new GregorianCalendar();
                dateFromTimestamp.setTimeInMillis((Long) eItemSnapshot.child("timestamp").getValue());

                retrievedTransactionItem.setDate(dateFromTimestamp);
                retrievedTransactionItem.getDate().getTimeInMillis();

                String currencyKey = (String) eItemSnapshot.child("amount").child("currencyKey").getValue();
                Double currencyAmount = eItemSnapshot.child("amount").child("currencyAmount").getValue(Double.class);
                retrievedTransactionItem.setAmount(new TransactionAmount(currencyKey, currencyAmount));

                retrievedTransactionItem.setId(eItemSnapshot.getKey());

                localTransactionItems.add(retrievedTransactionItem);
            }
            setValue(localTransactionItems);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    DatabaseReference databaseReference;

    public TransactionItemLiveData(DatabaseReference ref) {
        databaseReference = ref;
        localTransactionItems = new ArrayList<>();
    }

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        localTransactionItems.clear();
        databaseReference.removeEventListener(valueEventListener);
    }
}