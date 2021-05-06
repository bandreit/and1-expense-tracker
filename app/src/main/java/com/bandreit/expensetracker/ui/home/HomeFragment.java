package com.bandreit.expensetracker.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.RecyclerItemTouchHelper;
import com.bandreit.expensetracker.model.ExpenseItem.ExpenseItemAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText("Transactions");

        RecyclerView recyclerView = root.findViewById(R.id.expense_items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        ExpenseItemAdapter adapter = new ExpenseItemAdapter();
        recyclerView.setAdapter(adapter);

        homeViewModel.getGroupedExpenseItems().observe(getViewLifecycleOwner(), adapter::updateList);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }
}