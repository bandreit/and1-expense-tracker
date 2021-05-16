package com.bandreit.expensetracker.ui.dashboard.income;

import androidx.annotation.RequiresApi;
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

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistory;
import com.bandreit.expensetracker.model.expenseHistory.ExpenseHistoryAdapter;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class IncomeFragment extends Fragment {

    LineChart lineChart;
    LineData lineData;
    List<Entry> entryList = new ArrayList<>();
    private IncomeViewModel mViewModel;

    public static IncomeFragment newInstance() {
        return new IncomeFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.income_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);
        mViewModel.init();

        RecyclerView recyclerView = root.findViewById(R.id.income_history_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ExpenseHistoryAdapter adapter = new ExpenseHistoryAdapter();
        recyclerView.setAdapter(adapter);
        initializeChart(root);

        mViewModel.getAllExpenseItems().observe(getViewLifecycleOwner(), expenseItems -> {
//            if (expenseItems.size() == 0) {
//                noItemsText.setText(R.string.no_transactions);
//            } else {
//                noItemsText.setText("");
//            }
//            isLoading.setValue(false);
            expenseItems = expenseItems.stream()
                    .filter(c -> c.getType().equals(TransactionType.INCOME))
                    .collect(Collectors.toList());

            ArrayList<ExpenseHistory> expenseHistoryArrayList = mViewModel.filterExpenseItemsByCategory(expenseItems);
            adapter.updateList(expenseHistoryArrayList);
            loadChartData(expenseHistoryArrayList);

        });
        return root;
    }

    private void initializeChart(View root) {
        lineChart = root.findViewById(R.id.incomeLineChart);
        lineChart.setMinimumHeight(500);
        lineChart.getXAxis().setTextColor(Color.BLACK);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setLabelRotationAngle(45);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setAxisLineColor(Color.WHITE);
        lineChart.getAxisLeft().setTextColor(Color.BLACK);
        lineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }

    private void loadChartData(ArrayList<ExpenseHistory> expenseHistoryArrayList) {
//        for (ExpenseHistory expenseHistory : expenseHistoryArrayList) {
//            entryList.add(new Entry(expenseHistory.getDate().get(Calendar.MONTH) + 1, (float) expenseHistory.getAmount().getCurrencyAmount()));
//        }

        LineDataSet lineDataSet = new LineDataSet(entryList, "income");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setColors(ColorTemplate.rgb("1F6F84"));
        lineDataSet.setValueTextSize(18);
        lineDataSet.setValueTextColor(ColorTemplate.rgb("E9B44C"));
        lineDataSet.setLineWidth(4.0f);
        lineDataSet.setFillColor(ColorTemplate.rgb("1F6F84"));
        lineDataSet.setColor(ColorTemplate.rgb("243E36"));
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(255);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(10);
        lineChart.invalidate();
    }
}