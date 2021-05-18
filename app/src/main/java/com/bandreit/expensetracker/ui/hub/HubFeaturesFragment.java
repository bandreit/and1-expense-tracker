package com.bandreit.expensetracker.ui.hub;

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

public class HubFeaturesFragment extends Fragment {

    private HubViewModel hubViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hubViewModel =
                new ViewModelProvider(this).get(HubViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hub, container, false);
        TextView textView2 = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView2.setText(R.string.hub);

        LinearLayout taxBreakdownLayout = root.findViewById(R.id.taxBreakdownLayout);
        MaterialButton taxBreakdownButton = root.findViewById(R.id.taxBreakdownButton);
        LinearLayout savingsBreakdownLayout = root.findViewById(R.id.savingProgressLayout);
        MaterialButton savingsBreakdownButton = root.findViewById(R.id.savingsBreakdownButton);

        taxBreakdownLayout.setOnClickListener(v -> {
            navigateToTaxBreakDown();
        });

        taxBreakdownButton.setOnClickListener(v -> {
            navigateToTaxBreakDown();
        });

        savingsBreakdownLayout.setOnClickListener(v -> {
            navigateToSavingsBreakdown();
        });

        savingsBreakdownButton.setOnClickListener(v -> {
            navigateToSavingsBreakdown();
        });

        return root;
    }

    private void navigateToSavingsBreakdown() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_extra_features_to_savingsHubFragment);
    }

    private void navigateToTaxBreakDown() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_navigation_extra_features_to_taxesHubFragment);
    }
}