package com.bandreit.expensetracker.model.balance;

import com.bandreit.expensetracker.model.transactions.TransactionAmount;

public class Balance {
    private TransactionAmount transactionAmount;
    private long timestamp;

    public Balance(TransactionAmount transactionAmount, long timestamp) {
        this.transactionAmount = transactionAmount;
        this.timestamp = timestamp;
    }

    public Balance() {
    }

    public TransactionAmount getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(TransactionAmount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
