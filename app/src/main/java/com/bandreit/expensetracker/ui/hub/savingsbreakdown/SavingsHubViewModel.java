package com.bandreit.expensetracker.ui.hub.savingsbreakdown;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SavingsHubViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SavingsHubViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}