package com.bandreit.expensetracker.model.categories;

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
        categories.add(new Category("salary", "Salary", R.drawable.layout_bg, R.drawable.cash));
        categories.add(new Category("payment", "Payment", R.drawable.layout_bg2, R.drawable.cash_multiple));
        categories.add(new Category("debt", "Debt", R.drawable.layout_bg3, R.drawable.currency_usd_off));
        categories.add(new Category("general_revenue", "General revenue", R.drawable.layout_bg4, R.drawable.card));
        categories.add(new Category("donation", "Donation", R.drawable.layout_bg5, R.drawable.cash_100));
        categories.add(new Category("housing", "Housing", R.drawable.layout_bg6, R.drawable.home));
        categories.add(new Category("transportation", "Transportation", R.drawable.layout_bg7, R.drawable.train_car));
        categories.add(new Category("food", "Food", R.drawable.layout_bg8, R.drawable.food));
        categories.add(new Category("utilities", "Utilities", R.drawable.layout_bg9, R.drawable.hammer_screwdriver));
        categories.add(new Category("clothing", "Clothing", R.drawable.layout_bg10, R.drawable.tshirt_crew));
        categories.add(new Category("medical", "Medical", R.drawable.layout_bg11, R.drawable.needle));
        categories.add(new Category("insurance", "Insurance", R.drawable.layout_bg12, R.drawable.medical_bag));
        categories.add(new Category("personal", "Personal", R.drawable.layout_bg13, R.drawable.account_alert));
        categories.add(new Category("education", "Education", R.drawable.layout_bg14, R.drawable.school));
        categories.add(new Category("entertainment", "Entertainment", R.drawable.layout_bg15, R.drawable.music_circle));
        categories.add(new Category("gifts", "Gifts", R.drawable.layout_bg16, R.drawable.wallet_giftcard));
        ownCategories.setValue(categories);
    }

    public LiveData<List<Category>> getOwnCategories() {
        return ownCategories;
    }
}
