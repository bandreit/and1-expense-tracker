package com.bandreit.expensetracker.ui.addItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;

public class AddItemStep3Fragment extends Fragment {

    private AddItemViewModel addItemViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel = AddItemViewModel.getInstance();

        View root = inflater.inflate(R.layout.fragment_add_item3, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText(R.string.select_date);

        return root;
    }

}
