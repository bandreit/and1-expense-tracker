package com.bandreit.expensetracker.model.ExpenseItem;

import android.icu.util.CurrencyAmount;

import java.util.Currency;

public class ExpenseAmount {
    private Currency currency;
    private double currencyAmount;

    public ExpenseAmount(String currencyType, double currencyAmount) {
        this.currency = Currency.getInstance(currencyType);
        this.currencyAmount = currencyAmount;
    }

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
