package com.bandreit.expensetracker.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bandreit.expensetracker.ui.dashboard.expenses.ExpensesFragment;
import com.bandreit.expensetracker.ui.dashboard.income.IncomeFragment;

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    private final int numOfTabs;
    private String[] tabTitles = new String[]{"Expenses", "Income"};

    public DashboardPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ExpensesFragment.newInstance();
            case 1:
                return IncomeFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Generate title based on item position
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
