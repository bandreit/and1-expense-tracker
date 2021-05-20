package com.bandreit.expensetracker.model.expenseHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.transactions.TransactionType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;

public class ExpenseHistoryAdapter extends RecyclerView.Adapter<ExpenseHistoryAdapter.ViewHolder> {

    private List<ExpenseHistory> expenseHistoryList = new ArrayList<>();
    private View view;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.categorised_expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseHistory expenseHistory = expenseHistoryList.get(position);
        holder.title.setText(expenseHistory.getCategory().getName());
        Formatter formatter = new Formatter();
        formatter.format("%.2f", expenseHistory.getAmount().getCurrencyAmount());
        holder.amount.setText(getTypeOfExpense(expenseHistory.getType()) + formatter.toString() + expenseHistory.getAmount().getCurrency().getSymbol());
        holder.categoryImage.setImageResource(expenseHistory.getCategory().getImageId());
        String date = theMonth(expenseHistory.getDate().get(Calendar.MONTH)) + " " + (expenseHistory.getDate().get(Calendar.YEAR));
        holder.date.setText(date);
        holder.categoryImageBackground.setBackgroundResource(expenseHistory.getCategory().getBackgroundId());
    }

    @Override
    public int getItemCount() {
        return expenseHistoryList.size();
    }

    public void updateList(List<ExpenseHistory> expenseItems) {
        this.expenseHistoryList = expenseItems;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView category;
        private final TextView date;
        private final TextView amount;
        private final ImageView categoryImage;
        private final LinearLayout categoryImageBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.expense_item_title);
            category = itemView.findViewById(R.id.expense_item_category);
            date = itemView.findViewById(R.id.expense_item_date);
            amount = itemView.findViewById(R.id.expense_item_amount);
            categoryImage = itemView.findViewById(R.id.expense_item_category_image);
            categoryImageBackground = itemView.findViewById(R.id.ep_category_image_background);
        }
    }

    public String getTypeOfExpense(TransactionType transactionType) {
        return transactionType == TransactionType.EXPENSE ? "-" : "+";
    }

    public static String theMonth(int month) {
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

}
