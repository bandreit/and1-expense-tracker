package com.bandreit.expensetracker.ui.addItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bandreit.expensetracker.R;

public class AddItemFragmentStep1 extends Fragment {

    private AddItemViewModel addItemViewModel;
    private GridView gridView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel = new ViewModelProvider(this).get(AddItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_item, container, false);

//        TextView textView = ((MainActivity)getActivity()).findViewById(R.id.fragment_title);
//        textView.setText("Transactions");


//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
