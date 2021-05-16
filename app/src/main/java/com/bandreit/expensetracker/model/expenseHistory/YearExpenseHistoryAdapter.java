package com.bandreit.expensetracker.model.expenseHistory;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.categories.Category;
import com.bandreit.expensetracker.model.categories.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class YearExpenseHistoryAdapter extends RecyclerView.Adapter<YearExpenseHistoryAdapter.ViewHolder> {

    private List<Integer> years = new ArrayList<>();
    private final OnListItemClickListener mOnListItemClickListener;

    public YearExpenseHistoryAdapter(OnListItemClickListener listItemClickListener) {
        mOnListItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.year_wrapper, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer year = years.get(position);
        holder.year.setText(year.toString());
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public void updateList(List<Integer> years) {
        this.years = years;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            year = itemView.findViewById(R.id.year_section);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnListItemClickListener.onListItemClick(years.get(getAdapterPosition()));
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(Integer clickedYear);
    }
}
