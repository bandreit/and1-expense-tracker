package com.bandreit.expensetracker.model.transactions;


import android.net.Uri;

import com.bandreit.expensetracker.model.categories.Category;
import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class TransactionItem implements Comparable<TransactionItem> {
    private String id;
    private String title;
    private Category category;
    private Calendar date;
    private long timestamp;
    private TransactionAmount amount;
    private TransactionType type;
    private String imageUri;

    public TransactionItem(String title, Category category, Calendar date, TransactionAmount amount, TransactionType type) {
        this.title = title;
        this.category = category;
        this.date = date;
        timestamp = date.getTimeInMillis();
        this.amount = amount;
        this.type = type;
    }

    public TransactionItem() {
        this.title = null;
        this.category = null;
        this.date = null;
        this.timestamp = 0;
        this.amount = null;
        this.type = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    @Exclude
    public Calendar getDate() {
        return date;
    }

    public TransactionAmount getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setAmount(TransactionAmount amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(TransactionItem transactionItem) {
        if (getDate() == null || transactionItem.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(transactionItem.getDate());
    }
}
