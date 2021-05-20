package com.bandreit.expensetracker.model.exchange;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static ExchangeApi exchangeApi;

    public static ExchangeApi getExchangeApi() {
        if (exchangeApi == null) {
            exchangeApi = new Retrofit.Builder()
                    .baseUrl("http://data.fixer.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ExchangeApi.class);
        }
        return exchangeApi;
    }
}