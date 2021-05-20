package com.bandreit.expensetracker.model.exchange;

public class ExchangeResponse {
    private Rates rates;

    public double getRates() {
        return rates.DKK;
    }

    private class Rates {
        private double DKK;
    }

}
