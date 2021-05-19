package com.bandreit.expensetracker.ui.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bandreit.expensetracker.model.users.UserLiveData;
import com.bandreit.expensetracker.model.users.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel {
    private UserLiveData userLiveData;
    private UserRepository userRepository;

    public ProfileViewModel(Application app) {
        super(app);
        userLiveData = new UserLiveData();
        userRepository = UserRepository.getInstance(app);
    }

    public UserLiveData getUserLiveData() {
        return userLiveData;
    }

    public void signOut() {
        userRepository.signOut();
    }
}