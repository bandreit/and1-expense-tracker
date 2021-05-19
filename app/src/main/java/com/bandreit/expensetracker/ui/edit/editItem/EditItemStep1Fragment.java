package com.bandreit.expensetracker.ui.edit.editItem;

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

public class EditItemStep1Fragment extends Fragment implements CategoryAdapter.OnListItemClickListener {

    private EditItemViewModel editItemViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editItemViewModel = EditItemViewModel.getInstance();

        View root = inflater.inflate(R.layout.fragment_add_item1, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText(R.string.choose_category);

        CategoryAdapter adapter = new CategoryAdapter(this);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);

        editItemViewModel.getCategories().observe(getViewLifecycleOwner(), adapter::updateList);
        return root;
    }

    @Override
    public void onListItemClick(Category clickedCategory) {
        editItemViewModel.selectCategory(clickedCategory);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.editItemStep2Fragment);
    }
}
