package com.bandreit.expensetracker.ui.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.RecyclerItemTouchHelper;
import com.bandreit.expensetracker.model.balance.Balance;
import com.bandreit.expensetracker.model.expenseHistory.YearExpenseHistoryAdapter;
import com.bandreit.expensetracker.model.transactions.SectionOrRow;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionItemAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TransactionItemAdapter.OnListItemClickListener {

    private LineChart chart;
    private HomeViewModel homeViewModel;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private View root;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init();
        root = inflater.inflate(R.layout.fragment_home, container, false);
        chart = root.findViewById(R.id.balanceLineChart);

        isLoading.setValue(true);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText("Overview");
        progressBar = root.findViewById(R.id.transacionProgressBar);
        TextView noItemsText = root.findViewById(R.id.no_items_text);
        TextView currentBalance = root.findViewById(R.id.current_balance);
        TextView currency = root.findViewById(R.id.balance_currency);
        currency.setText(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("preferred_currency", "DKK"));

        initializeChart();
        RecyclerView recyclerView = root.findViewById(R.id.expense_items_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TransactionItemAdapter adapter = new TransactionItemAdapter(this);
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

        homeViewModel.getBalanceHistory().observe(getViewLifecycleOwner(), balances -> {
            if (balances.size() != 0)
                currentBalance.setText(String.valueOf(balances.get(balances.size() - 1).getTransactionAmount().getCurrencyAmount()));
            loadBalanceChart(balances);
        });

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    private void initializeChart() {
        chart.setViewPortOffsets(0, 0, 0, 0);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(false);

        YAxis y = chart.getAxisLeft();
        y.setTextColor(Color.BLUE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void loadBalanceChart(List<Balance> balances) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < balances.size(); i++) {
            values.add(new Entry(i, (float) balances.get(i).getTransactionAmount().getCurrencyAmount()));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(.2f);
            set1.setDrawCircles(false);
            set1.setLineWidth(5f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(R.color.darksalmon);
            int color = ContextCompat.getColor(getContext(), R.color.blue);
            set1.setColor(color);

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
            chart.invalidate();
        }
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onListItemClick(SectionOrRow clickedRow) {
        PopUpTransaction popUpClass = new PopUpTransaction(clickedRow, homeViewModel, getViewLifecycleOwner());
        popUpClass.showPopupWindow(root);
    }
}