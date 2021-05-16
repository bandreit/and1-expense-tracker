package com.bandreit.expensetracker.ui.dashboard.expenses;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistory;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistoryAdapter;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.bandreit.expensetracker.ui.dashboard.HidingScrollListener;
import com.bandreit.expensetracker.ui.dashboard.MonthFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExpensesFragment extends Fragment {

    BarChart barChart;
    List<BarEntry> entryList = new ArrayList<>();
    private ExpensesViewModel mViewModel;
    private HidingScrollListener listener;
    private View root;
    private ArrayList<ExpenseHistory> expenseHistoryArrayList;

    public static ExpensesFragment newInstance() {
        return new ExpensesFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.expenses_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
        mViewModel.init();

        RecyclerView recyclerView = root.findViewById(R.id.expense_history_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        ExpenseHistoryAdapter adapter = new ExpenseHistoryAdapter();
        recyclerView.setAdapter(adapter);
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

        recyclerView.addOnScrollListener(listener);

        mViewModel.getAllExpenseItems().observe(getViewLifecycleOwner(), expenseItems -> {
//            if (expenseItems.size() == 0) {
//                noItemsText.setText(R.string.no_transactions);
//            } else {
//                noItemsText.setText("");
//            }
//            isLoading.setValue(false);
            expenseItems = expenseItems.stream()
                    .filter(c -> c.getType().equals(TransactionType.EXPENSE))
                    .collect(Collectors.toList());

            expenseHistoryArrayList = mViewModel.filterTransactionItemsByCategoryAndDate(expenseItems);
            adapter.updateList(expenseHistoryArrayList);
            loadChartData(expenseHistoryArrayList);
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

        return root;
    }

    private void initializeChart(View root) {
        barChart = root.findViewById(R.id.expenseLineChart);
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
    private void loadChartData(ArrayList<ExpenseHistory> expenseHistoryArrayList) {
//        ArrayList<ExpenseHistory> list = new ArrayList<>();
//        for (ExpenseHistory c : expenseHistoryArrayList) {
//            if ((c.getDate().get(Calendar.YEAR) == 2021)) {
//                list.add(c);
//            }
//        }
//        expenseHistoryArrayList = list;

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
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        set1.setDrawValues(true);
        set1.setValueTextSize(18);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(10);
        barChart.invalidate();
    }

    public void showViews() {
        listener.resetScrollDistance();
        root.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setDuration(1000);
    }

    public void hideViews() {
        root.animate().translationY(-1 * barChart.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }
}