package com.bandreit.expensetracker.model.balance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BalanceLiveData extends LiveData<List<Balance>> {
    private final ArrayList<Balance> localBalances;
    DatabaseReference databaseReference;

    public BalanceLiveData(DatabaseReference ref) {
        databaseReference = ref;
        localBalances = new ArrayList<>();
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            localBalances.clear();
            for (DataSnapshot balanceSnapshot : snapshot.getChildren()) {
                Balance retrievedBalance = balanceSnapshot.getValue(Balance.class);
                localBalances.add(retrievedBalance);
            }
            setValue(localBalances);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        localBalances.clear();
        databaseReference.removeEventListener(valueEventListener);
    }
}