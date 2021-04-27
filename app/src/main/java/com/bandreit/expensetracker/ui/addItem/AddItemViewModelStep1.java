package com.bandreit.expensetracker.ui.addItem;

import android.widget.GridView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddItemViewModelStep1 extends ViewModel {

    private MutableLiveData<String> mText;

    public AddItemViewModelStep1() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}