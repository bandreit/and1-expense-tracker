package com.bandreit.expensetracker.model.ExpenseItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.R;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

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
            h.category.setText(expenseItem.getCategory());
            h.categoryImage.setImageResource(expenseItem.getImageId());
            String date = "feb" + " " + expenseItem.getDate().getDay();
            h.date.setText(date);
            h.categoryImageBackground.setBackgroundResource(BACKGROUNDS[(int) (Math.random() * (5))]);
        } else {
            SectionViewHolder h = (SectionViewHolder) holder;
            h.deviderText.setText(item.getSection());
        }
    }

    public String getTypeOfExpense(ExpenseType expenseType) {
        return expenseType == ExpenseType.EXPENSE ? "-" : "+";
    }

    @Override
    public int getItemCount() {
        return sectionOrRows.size();
    }

    public void updateList(List<SectionOrRow> expenseItems) {
        this.sectionOrRows = expenseItems;
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
            categoryImageBackground = itemView.findViewById(R.id.category_image_background);
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
//        ExpenseItem expenseItem = sectionOrRows.get(position);
//        sectionOrRows.remove(expenseItem);
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
}
