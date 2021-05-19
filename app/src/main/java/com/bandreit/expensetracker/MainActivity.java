package com.bandreit.expensetracker;

import android.content.Intent;
import android.os.Bundle;

import com.bandreit.expensetracker.ui.signIn.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        checkIfSignedIn();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.add_item_1, R.id.navigation_extra_features, R.id.profileFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void checkIfSignedIn() {
        viewModel.getCurrentUser().observe(this, user -> {
            if (user == null)
                startLoginActivity();
        });
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

}