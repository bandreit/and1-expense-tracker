package com.bandreit.expensetracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bandreit.expensetracker.model.users.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public MainActivityViewModel(Application app) {
        super(app);
        userRepository = UserRepository.getInstance(app);
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return userRepository.getCurrentUser();
    }
}