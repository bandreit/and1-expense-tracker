package com.bandreit.expensetracker.model.categories;

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
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private final OnListItemClickListener mOnListItemClickListener;

    public CategoryAdapter(OnListItemClickListener listener) {
        mOnListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryImage.setImageResource(categories.get(position).getImageId());
        holder.categoryImageBackground.setBackgroundResource(categories.get(position).getBackgroundId());
        holder.categoryName.setText(categories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView categoryImage;
        private final LinearLayout categoryImageBackground;
        private final TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryImageBackground = itemView.findViewById(R.id.category_image_background);
            categoryName = itemView.findViewById(R.id.category_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(categories.get(getAdapterPosition()));
        }
    }

    public void updateList(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public interface OnListItemClickListener {
        void onListItemClick(Category clickedCategory);
    }
}
