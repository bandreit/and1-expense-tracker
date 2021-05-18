package com.bandreit.expensetracker.ui.hub.savingsbreakdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.google.android.material.button.MaterialButton;

public class SavingsHubFragment extends Fragment {

    private SavingsHubViewModel savingsHubViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        savingsHubViewModel =
                new ViewModelProvider(this).get(SavingsHubViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hub_savings, container, false);
        TextView textView2 = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView2.setText(R.string.savings_tracker);
        return root;
    }

}