package com.bandreit.expensetracker.model.transactions;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.ui.edit.editItem.EditItemViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private List<SectionOrRow> sectionOrRows = new ArrayList<>();
    private final OnListItemClickListener mOnListItemClickListener;

    public TransactionItemAdapter(OnListItemClickListener mOnListItemClickListener) {
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

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
            TransactionItem transactionItem = item.getRow();
            RowViewHolder h = (RowViewHolder) holder;
            h.title.setText(transactionItem.getTitle());
            Formatter formatter = new Formatter();
            formatter.format("%.2f", transactionItem.getAmount().getCurrencyAmount());
            h.amount.setText(getTypeOfExpense(transactionItem.getType()) + formatter.toString() + transactionItem.getAmount().getCurrency().getSymbol());
            h.category.setText(transactionItem.getCategory().getName());
            h.categoryImage.setImageResource(transactionItem.getCategory().getImageId());
            String date = transactionItem.getDate().get(Calendar.DATE) + " " + theMonth(transactionItem.getDate().get(Calendar.MONTH));
            h.date.setText(date);
            h.categoryImageBackground.setBackgroundResource(transactionItem.getCategory().getBackgroundId());
        } else {
            SectionViewHolder h = (SectionViewHolder) holder;
            Calendar date = item.getSection();
            h.deviderText.setText(date.get(Calendar.DATE) + " " + date.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + date.get(Calendar.YEAR));
        }
    }

    public String getTypeOfExpense(TransactionType transactionType) {
        return transactionType == TransactionType.EXPENSE ? "-" : "+";
    }

    @Override
    public int getItemCount() {
        return sectionOrRows.size();
    }

    public void updateList(List<TransactionItem> allTransactionItems) {
        Map<Calendar, List<TransactionItem>> map = new HashMap<>();

        if (allTransactionItems != null) {
            for (TransactionItem eItem : allTransactionItems) {
                Calendar key = eItem.getDate();

                key.set(Calendar.HOUR_OF_DAY, 0);
                key.set(Calendar.MINUTE, 0);
                key.set(Calendar.SECOND, 0);
                key.set(Calendar.MILLISECOND, 0);

                if (map.containsKey(key)) {
                    List<TransactionItem> list = map.get(key);
                    list.add(eItem);
                } else {
                    List<TransactionItem> list = new ArrayList<TransactionItem>();
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
            List<TransactionItem> itemsByDate = map.get(date);
            Collections.sort(itemsByDate);
            Collections.reverse(itemsByDate);
            for (TransactionItem transactionItem : itemsByDate) {
                groupedExpenseItemsList.add(SectionOrRow.createRow(transactionItem));
            }
        }

        this.sectionOrRows = groupedExpenseItemsList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return view.getContext();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnListItemClickListener.onListItemClick(sectionOrRows.get(getAdapterPosition()));
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviderText;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            deviderText = (TextView) itemView.findViewById(R.id.section_divider);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteItem(int position) {
        SectionOrRow row = sectionOrRows.get(position);
        TransactionItem transactionItem = row.getRow();
        TransactionItemRepository.getInstance().deleteExpense(transactionItem.getId());
        sectionOrRows.remove(row);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TransactionItem transactionItem = sectionOrRows.get(position).getRow();
        EditItemViewModel.getInstance().editLocalExpenseItem(transactionItem);
        Navigation.findNavController((Activity) view.getContext(), R.id.nav_host_fragment).navigate(R.id.editItemStep1Fragment);
        notifyItemChanged(position);
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

    public interface OnListItemClickListener {
        void onListItemClick(SectionOrRow clickedRow);
    }
}
