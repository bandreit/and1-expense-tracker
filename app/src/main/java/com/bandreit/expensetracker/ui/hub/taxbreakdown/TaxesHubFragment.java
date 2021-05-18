package com.bandreit.expensetracker.ui.hub.taxbreakdown;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.stream.Collectors;

public class TaxesHubFragment extends Fragment {

    private TaxesHubViewModel taxesHubViewModel;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taxesHubViewModel =
                new ViewModelProvider(this).get(TaxesHubViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hub_taxes, container, false);
        TextView textView2 = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView2.setText(R.string.taxes_breakdown);

        EditText inputSU = root.findViewById(R.id.inputSU);
        EditText inputSalary = root.findViewById(R.id.inputSalary);

        TextView income_salary_a = root.findViewById(R.id.income_salary_a);
        TextView income_su_a = root.findViewById(R.id.income_su_a);
        TextView income_salary_b = root.findViewById(R.id.income_salary_b);
        TextView income_su_b = root.findViewById(R.id.income_su_b);

        TextView income_tax_a = root.findViewById(R.id.income_tax_a);
        TextView income_tax_b = root.findViewById(R.id.income_tax_b);

        inputSU.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                taxesHubViewModel.setSuAmount(Double.valueOf(s.toString()));
            }
        });
        inputSalary.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                taxesHubViewModel.setSalaryAmount(Double.valueOf(s.toString()));
            }
        });

        taxesHubViewModel.incomeSalaryA.observe(getViewLifecycleOwner(), incomeSalaryA -> {
            income_salary_a.setText(incomeSalaryA.toString());
        });

        taxesHubViewModel.incomeSalaryB.observe(getViewLifecycleOwner(), incomeSalaryB -> {
            income_salary_b.setText(incomeSalaryB.toString());
        });

        taxesHubViewModel.incomeSuA.observe(getViewLifecycleOwner(), incomeSuA -> {
            income_su_a.setText(incomeSuA.toString());
        });

        taxesHubViewModel.incomeSuB.observe(getViewLifecycleOwner(), incomeSuB -> {
            income_su_b.setText(incomeSuB.toString());
        });

        taxesHubViewModel.taxIncomeA.observe(getViewLifecycleOwner(), taxIncomeA -> {
            income_tax_a.setText(taxIncomeA.toString());
        });

        taxesHubViewModel.taxIncomeB.observe(getViewLifecycleOwner(), taxIncomeB -> {
            income_tax_b.setText(taxIncomeB.toString());
        });

        return root;
    }

}