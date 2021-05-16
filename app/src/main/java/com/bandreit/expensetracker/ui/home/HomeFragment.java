package com.bandreit.expensetracker.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.RecyclerItemTouchHelper;
import com.bandreit.expensetracker.model.transactions.TransactionItemAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        isLoading.setValue(true);

        progressBar = root.findViewById(R.id.transacionProgressBar);
        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        TextView noItemsText = root.findViewById(R.id.no_items_text);
        textView.setText("Transactions");

        RecyclerView recyclerView = root.findViewById(R.id.expense_items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        TransactionItemAdapter adapter = new TransactionItemAdapter();
        recyclerView.setAdapter(adapter);

        homeViewModel.getGroupedExpenseItems().observe(getViewLifecycleOwner(), expenseItems -> {
            if (expenseItems.size() == 0) {
                noItemsText.setText(R.string.no_transactions);
            } else {
                noItemsText.setText("");
            }
            isLoading.setValue(false);
            adapter.updateList(expenseItems);
        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
        });

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

}