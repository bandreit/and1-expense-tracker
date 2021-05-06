package com.bandreit.expensetracker.model.ExpenseItem;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpenseItemLiveData extends LiveData<List<ExpenseItem>> {
    private final ArrayList<ExpenseItem> localExpenseItems;

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            System.out.println("IN VALUE EVEN LISTENER");
            localExpenseItems.clear();
            for (DataSnapshot eItemSnapshot : snapshot.getChildren()) {

                ExpenseItem retrievedExpenseItem = eItemSnapshot.getValue(ExpenseItem.class);
                Calendar dateFromTimestamp = Calendar.getInstance();
                dateFromTimestamp.setTimeInMillis((Long) eItemSnapshot.child("timestamp").getValue());
                retrievedExpenseItem.setDate(dateFromTimestamp);

                String currencyKey = (String) eItemSnapshot.child("amount").child("currencyKey").getValue();
                Double currencyAmount = eItemSnapshot.child("amount").child("currencyAmount").getValue(Double.class);
                retrievedExpenseItem.setAmount(new ExpenseAmount(currencyKey, currencyAmount));

                localExpenseItems.add(retrievedExpenseItem);
            }
            setValue(localExpenseItems);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            ExpenseItem addedExpenseItem = dataSnapshot.getValue(ExpenseItem.class);
            Calendar dateFromTimestamp = Calendar.getInstance();
            dateFromTimestamp.setTimeInMillis((Long) dataSnapshot.child("timestamp").getValue());
            addedExpenseItem.setDate(dateFromTimestamp);

            String currencyKey = (String) dataSnapshot.child("amount").child("currencyKey").getValue();
            Double currencyAmount = dataSnapshot.child("amount").child("currencyAmount").getValue(Double.class);
            addedExpenseItem.setAmount(new ExpenseAmount(currencyKey, currencyAmount));

            addedExpenseItem.setId(dataSnapshot.getKey());
            localExpenseItems.add(addedExpenseItem);
            setValue(localExpenseItems);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    DatabaseReference databaseReference;

    public ExpenseItemLiveData(DatabaseReference ref) {
        databaseReference = ref;
        localExpenseItems = new ArrayList<>();
    }

    @Override
    protected void onActive() {
        super.onActive();
//        databaseReference.addValueEventListener(valueEventListener);
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
//        databaseReference.removeEventListener(valueEventListener);
        databaseReference.removeEventListener(childEventListener);
    }
}