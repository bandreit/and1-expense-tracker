package com.bandreit.expensetracker.model.ExpenseItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpenseItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int[] BACKGROUNDS = {R.drawable.layout_bg, R.drawable.layout_bg2, R.drawable.layout_bg3, R.drawable.layout_bg4, R.drawable.layout_bg5};
    private View view;
    private List<SectionOrRow> sectionOrRows = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
            return new RowViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_item, parent, false);
            return new SectionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionOrRow item = sectionOrRows.get(position);
        if (item.isRow()) {
            ExpenseItem expenseItem = item.getRow();
            RowViewHolder h = (RowViewHolder) holder;
            h.title.setText(expenseItem.getTitle());
            Formatter formatter = new Formatter();
            formatter.format("%.2f", expenseItem.getAmount().getCurrencyAmount());
            h.amount.setText(getTypeOfExpense(expenseItem.getType()) + expenseItem.getAmount().getCurrency().getSymbol() + formatter.toString());
            h.category.setText(expenseItem.getCategory().getName());
            h.categoryImage.setImageResource(expenseItem.getCategory().getImageId());
            String date = expenseItem.getDate().get(Calendar.DATE) + " " + theMonth(expenseItem.getDate().get(Calendar.MONTH));
            h.date.setText(date);
            h.categoryImageBackground.setBackgroundResource(expenseItem.getCategory().getBackgroundId());
        } else {
            SectionViewHolder h = (SectionViewHolder) holder;
            Calendar date = item.getSection();
            h.deviderText.setText(date.get(Calendar.DATE) + " " + date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + date.get(Calendar.YEAR));
        }
    }

    public String getTypeOfExpense(ExpenseType expenseType) {
        return expenseType == ExpenseType.EXPENSE ? "-" : "+";
    }

    @Override
    public int getItemCount() {
        return sectionOrRows.size();
    }

    public void updateList(List<ExpenseItem> allExpenseItems) {
        Map<Calendar, List<ExpenseItem>> map = new HashMap<>();

        if (allExpenseItems != null) {
            for (ExpenseItem eItem : allExpenseItems) {
                Calendar key = eItem.getDate();

                if (map.containsKey(key)) {
                    List<ExpenseItem> list = map.get(key);
                    list.add(eItem);
                } else {
                    List<ExpenseItem> list = new ArrayList<ExpenseItem>();
                    list.add(eItem);
                    map.put(key, list);
                }
            }
        }

        ArrayList<SectionOrRow> groupedExpenseItemsList = new ArrayList<>();

        List<Calendar> toSortBy = new ArrayList<>(map.keySet());
        Collections.sort(toSortBy);
        Collections.reverse(toSortBy);

        for (Calendar date : toSortBy) {
            groupedExpenseItemsList.add(SectionOrRow.createSection(date));
            for (ExpenseItem expenseItem : map.get(date)) {
                groupedExpenseItemsList.add(SectionOrRow.createRow(expenseItem));
            }
        }

        this.sectionOrRows = groupedExpenseItemsList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return view.getContext();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView category;
        private final TextView date;
        private final TextView amount;
        private final ImageView categoryImage;
        private final LinearLayout categoryImageBackground;

        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.expense_item_title);
            category = itemView.findViewById(R.id.expense_item_category);
            date = itemView.findViewById(R.id.expense_item_date);
            amount = itemView.findViewById(R.id.expense_item_amount);
            categoryImage = itemView.findViewById(R.id.expense_item_category_image);
            categoryImageBackground = itemView.findViewById(R.id.ep_category_image_background);
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView deviderText;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            deviderText = (TextView) itemView.findViewById(R.id.section_divider);
        }
    }

    public void deleteItem(int position) {
        SectionOrRow row = sectionOrRows.get(position);
        ExpenseItem expenseItem = row.getRow();
        ExpenseItemRepository.getInstance().deleteExpense(expenseItem.getId());
        sectionOrRows.remove(row);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        SectionOrRow item = sectionOrRows.get(position);
        if (!item.isRow()) {
            return 0;
        } else {
            return 1;
        }
    }

    public static String theMonth(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }
}
