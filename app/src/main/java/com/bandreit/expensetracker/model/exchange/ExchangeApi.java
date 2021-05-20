package com.bandreit.expensetracker.model.exchange;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExchangeApi {
    @GET("api/latest?access_key=ac6d3ea8345c8b75f01e5fd4e8e11aef&format=1&symbols=DKK")
    Call<ExchangeResponse> getExchange();
}