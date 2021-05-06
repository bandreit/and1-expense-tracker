package com.bandreit.expensetracker.model.Categories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bandreit.expensetracker.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final List<Category> categories;
    private final MutableLiveData<List<Category>> ownCategories = new MutableLiveData<>();

    public CategoryRepository() {
        this.categories = new ArrayList<>();
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_baseline_emoji_objects_24));
        categories.add(new Category("technology", "Technology", R.drawable.layout_bg2, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg3, R.drawable.ic_baseline_emoji_objects_24));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg4, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg5, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg2, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg4, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg3, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg4, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        categories.add(new Category("salary_and_payment", "Salary & Payment", R.drawable.layout_bg, R.drawable.ic_asvigi));
        ownCategories.setValue(categories);
    }

    public LiveData<List<Category>> getOwnCategories() {
        return ownCategories;
    }
}
