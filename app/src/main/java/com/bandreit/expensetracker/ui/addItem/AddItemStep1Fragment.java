package com.bandreit.expensetracker.ui.addItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.categories.CategoryAdapter;

public class AddItemStep1Fragment extends Fragment implements CategoryAdapter.OnListItemClickListener {

    private AddItemViewModel addItemViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel = AddItemViewModel.getInstance();


        View root = inflater.inflate(R.layout.fragment_add_item1, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText(R.string.choose_category);

        CategoryAdapter adapter = new CategoryAdapter(this);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        addItemViewModel.getCategories().observe(getViewLifecycleOwner(), adapter::updateList);
        return root;
    }


    @Override
    public void onListItemClick(Category clickedCategory) {
        addItemViewModel.selectCategory(clickedCategory);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.add_item_2);
    }
}
