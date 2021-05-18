package com.bandreit.expensetracker.ui.dashboard.income;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistory;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistoryAdapter;
import com.bandreit.expensetracker.model.expenseHistory.YearExpenseHistoryAdapter;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.bandreit.expensetracker.ui.dashboard.HidingScrollListener;
import com.bandreit.expensetracker.ui.dashboard.MonthFormatter;
import com.bandreit.expensetracker.ui.dashboard.expenses.ExpensesViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncomeFragment extends Fragment implements YearExpenseHistoryAdapter.OnListItemClickListener {

    private BarChart barChart;
    private IncomeViewModel mViewModel;
    private HidingScrollListener listener;
    private View root;
    private ArrayList<ExpenseHistory> expenseHistoryArrayList;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView yearRecyclerView;
    private ArrayList<Integer> yearsArrayList;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public static IncomeFragment newInstance() {
        return new IncomeFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.income_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);
        mViewModel.init();

        progressBar = root.findViewById(R.id.transacionProgressBar);
        isLoading.setValue(true);
        TextView noItemsText = root.findViewById(R.id.no_items_text);
        categoriesRecyclerView = root.findViewById(R.id.income_history_recycle_view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ExpenseHistoryAdapter adapter = new ExpenseHistoryAdapter();
        categoriesRecyclerView.setAdapter(adapter);
        categoriesRecyclerView.setHasFixedSize(false);

        yearRecyclerView = root.findViewById(R.id.year_recycle_view_income);
        yearRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false));
        YearExpenseHistoryAdapter yearExpenseHistoryAdapter = new YearExpenseHistoryAdapter(this);
        yearRecyclerView.setAdapter(yearExpenseHistoryAdapter);
        initializeChart(root);
        int threshold = 50;
        listener = new HidingScrollListener(threshold) {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        };

        categoriesRecyclerView.addOnScrollListener(listener);

        mViewModel.getAllExpenseItems().observe(getViewLifecycleOwner(), expenseItems -> {
            if (expenseItems.size() == 0) {
                noItemsText.setText(R.string.no_transactions);
            } else {
                noItemsText.setText("");
            }
            expenseItems = expenseItems.stream()
                    .filter(c -> c.getType().equals(TransactionType.INCOME))
                    .collect(Collectors.toList());

            updateYearsList(yearExpenseHistoryAdapter, expenseItems);
            filterAndLoadChartData(adapter, expenseItems, mViewModel.getSelectedYear().getValue());
        });

        mViewModel.selectedYear.observe(getViewLifecycleOwner(), selectedYear -> {
            List<TransactionItem> expenseItems = mViewModel.getAllExpenseItems().getValue().stream()
                    .filter(c -> c.getType().equals(TransactionType.INCOME))
                    .collect(Collectors.toList());

            updateYearsList(yearExpenseHistoryAdapter, expenseItems);
            filterAndLoadChartData(adapter, expenseItems, selectedYear);
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int selectedMonth = (int) e.getX();

                ArrayList<ExpenseHistory> expenseHistoryArrayListBySpecificMonth = (ArrayList<ExpenseHistory>) expenseHistoryArrayList.stream()
                        .filter(c -> c.getDate().getTime().getMonth() == selectedMonth)
                        .collect(Collectors.toList());
                adapter.updateList(expenseHistoryArrayListBySpecificMonth);
            }

            @Override
            public void onNothingSelected() {
                adapter.updateList(expenseHistoryArrayList);
            }
        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
        });

        return root;
    }

    private void updateYearsList(YearExpenseHistoryAdapter yearExpenseHistoryAdapter, List<TransactionItem> expenseItems) {
        yearsArrayList = new ArrayList<>();
        for (TransactionItem tItem : expenseItems
        ) {
            int yearToAdd = tItem.getDate().get(Calendar.YEAR);
            if (!yearsArrayList.contains(yearToAdd))
                yearsArrayList.add(yearToAdd);
            Collections.sort(yearsArrayList);
            Collections.reverse(yearsArrayList);
        }
        isLoading.setValue(false);
        yearExpenseHistoryAdapter.updateList(yearsArrayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterAndLoadChartData(ExpenseHistoryAdapter adapter, List<TransactionItem> expenseItems, Integer selectedYear) {
        expenseHistoryArrayList = mViewModel.filterTransactionItemsByCategoryAndDate(expenseItems);
        adapter.updateList(expenseHistoryArrayList);
        loadChartData(expenseHistoryArrayList, selectedYear);
    }

    private void initializeChart(View root) {
        barChart = root.findViewById(R.id.incomeLineChart);
        barChart.setMinimumHeight(500);
        barChart.getXAxis().setTextColor(Color.BLACK);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setLabelRotationAngle(45);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisLineColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.BLACK);
        barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new MonthFormatter());
        barChart.animateY(1500);
        barChart.setPinchZoom(true);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadChartData(ArrayList<ExpenseHistory> expenseHistoryArrayList, Integer selectedYear) {
        List<BarEntry> entryList = new ArrayList<>();

        if (selectedYear == null) selectedYear = 2021;
        ArrayList<ExpenseHistory> list = new ArrayList<>();
        for (ExpenseHistory c : expenseHistoryArrayList) {
            if (c.getDate().get(Calendar.YEAR) == selectedYear) {
                list.add(c);
            }
        }
        expenseHistoryArrayList = list;

        Map<Integer, List<ExpenseHistory>> expenseHistoriesByMonth = mViewModel.filterTransactionItemsByMonth(expenseHistoryArrayList);

        List<Integer> datesToSortBy = new ArrayList<>(expenseHistoriesByMonth.keySet());
        Collections.sort(datesToSortBy);

        for (Integer month : datesToSortBy) {
            List<ExpenseHistory> itemsByDate = expenseHistoriesByMonth.get(month);

            float sum = 0;
            for (ExpenseHistory expenseHistory : itemsByDate) {
                sum += expenseHistory.getAmount().getCurrencyAmount();
            }
            entryList.add(new BarEntry(month, sum));
        }

        BarDataSet set1;
        set1 = new BarDataSet(entryList, "Expenses");
        set1.setColors(ColorTemplate.PASTEL_COLORS);
        set1.setDrawValues(true);
        set1.setValueTextSize(18);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(12);
        barChart.invalidate();
    }

    public void showViews() {
        listener.resetScrollDistance();
        root.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setDuration(1000);
    }

    public void hideViews() {
        int heightToScroll = barChart.getHeight() + yearRecyclerView.getHeight();

//        Fancy scroll on top of chart, not working properly.
//        root.animate().translationY(-1 * heightToScroll).setInterpolator(new AccelerateInterpolator(2));
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    @Override
    public void onListItemClick(Integer clickedYear) {
        mViewModel.setSelectedYear(clickedYear);
    }
}