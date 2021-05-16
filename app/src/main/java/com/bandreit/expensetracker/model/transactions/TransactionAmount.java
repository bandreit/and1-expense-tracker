package com.bandreit.expensetracker.model.transactions;

import com.google.firebase.database.Exclude;

import java.util.Currency;

public class TransactionAmount {
    private Currency currency;
    private String currencyKey;
    private double currencyAmount;

    public TransactionAmount(String currencyType, double currencyAmount) {
        this.currencyKey = currencyType;
        this.currency = Currency.getInstance(currencyType);
        this.currencyAmount = currencyAmount;
    }

    public TransactionAmount() {
    }

    public String getCurrencyKey() {
        return currencyKey;
    }

    public void setCurrencyKey(String currencyKey) {
        this.currencyKey = currencyKey;
    }

    @Exclude
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(double currencyAmount) {
        this.currencyAmount = currencyAmount;
    }
}
