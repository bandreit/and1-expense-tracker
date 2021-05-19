package com.bandreit.expensetracker.ui.home;

import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.transactions.SectionOrRow;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PopUpTransaction {
    private TransactionItem selectedTransactionItem;

    public PopUpTransaction(SectionOrRow clickedRow) {
        selectedTransactionItem = clickedRow.getRow();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showPopupWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView image = popupView.findViewById(R.id.pop_expense_item_image);
        ImageView categoryImage = popupView.findViewById(R.id.pop_expense_item_category_image);
        LinearLayout categoryBackground = popupView.findViewById(R.id.pop_ep_category_image_background);
        TextView date = popupView.findViewById(R.id.pp_expense_item_date);
        TextView category = popupView.findViewById(R.id.pop_expense_item_category);
        TextView title = popupView.findViewById(R.id.pop_expense_item_title);
        TextView amount = popupView.findViewById(R.id.pop_expense_item_amount);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        date.setText(sdf.format(selectedTransactionItem.getDate().getTime()));
        category.setText(selectedTransactionItem.getCategory().getName());
        title.setText(selectedTransactionItem.getTitle());
        amount.setText(String.valueOf(selectedTransactionItem.getAmount().getCurrencyAmount()));
        categoryImage.setBackgroundResource(selectedTransactionItem.getCategory().getImageId());
        categoryBackground.setBackgroundResource(selectedTransactionItem.getCategory().getBackgroundId());

        Picasso.get()
                .load(selectedTransactionItem.getImageUri())
                .placeholder(R.drawable.loading)
                .error(R.drawable.graph)
                .into(image);
        Button buttonEdit = popupView.findViewById(R.id.closeButton);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
