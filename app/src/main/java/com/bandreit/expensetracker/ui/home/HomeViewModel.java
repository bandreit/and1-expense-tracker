package com.bandreit.expensetracker.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bandreit.expensetracker.model.balance.Balance;
import com.bandreit.expensetracker.model.balance.BalanceRepository;
import com.bandreit.expensetracker.model.exchange.ExchangeApi;
import com.bandreit.expensetracker.model.exchange.ExchangeResponse;
import com.bandreit.expensetracker.model.exchange.ServiceGenerator;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemRepository;
import com.bandreit.expensetracker.model.users.UserRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
    private final TransactionItemRepository transactionItemRepository;
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<Double> exchangeValue;

    public HomeViewModel(Application app) {
        super(app);
        this.userRepository = UserRepository.getInstance(app);
        this.transactionItemRepository = TransactionItemRepository.getInstance();
        this.balanceRepository = BalanceRepository.getInstance();
        exchangeValue = new MutableLiveData<>();
    }

    public void init() {
        String userId = userRepository.getCurrentUser().getValue().getUid();
        transactionItemRepository.init(userId);
        balanceRepository.init(userId);
    }

    public LiveData<List<TransactionItem>> getGroupedExpenseItems() {
        return transactionItemRepository.getAllExpenseItem();
    }

    public LiveData<List<Balance>> getBalanceHistory() {
        return balanceRepository.getAllBalanceHistory();
    }

    public MutableLiveData<Double> getExchangeValue() {
        return exchangeValue;
    }

    public void getExchangeRate() {
        ExchangeApi exchangeApi = ServiceGenerator.getExchangeApi();
        Call<ExchangeResponse> call = exchangeApi.getExchange();
        call.enqueue(new Callback<ExchangeResponse>() {
            @Override
            public void onResponse(Call<ExchangeResponse> call, Response<ExchangeResponse> response) {
                if (response.isSuccessful()) {
                    exchangeValue.setValue(response.body().getRates());
                }
            }

            @Override
            public void onFailure(Call<ExchangeResponse> call, Throwable t) {
                t.printStackTrace();
                Log.i("Retrofit", "Something went wrong :( " + t.getMessage());
            }
        });
    }
}