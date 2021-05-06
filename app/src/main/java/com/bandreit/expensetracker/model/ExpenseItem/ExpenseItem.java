package com.bandreit.expensetracker.model.ExpenseItem;


import com.bandreit.expensetracker.model.Categories.Category;
import com.google.firebase.database.Exclude;

import java.util.Calendar;

public class ExpenseItem implements Comparable<ExpenseItem> {
    private String id;
    private String title;
    private Category category;
    private Calendar date;
    private long timestamp;
    private ExpenseAmount amount;
    private ExpenseType type;
//    private int imageId;

    public ExpenseItem(String title, Category category, Calendar date, ExpenseAmount amount, ExpenseType type) {
        this.title = title;
        this.category = category;
        this.date = date;
        timestamp = date.getTimeInMillis();
        this.amount = amount;
        this.type = type;
//        this.imageId = imageId;
    }

    public ExpenseItem() {
        this.title = null;
        this.category = null;
        this.date = null;
        this.timestamp = 0;
        this.amount = null;
        this.type = null;
//        this.imageId = -1;
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

    public ExpenseAmount getAmount() {
        return amount;
    }

    public ExpenseType getType() {
        return type;
    }

//    public int getImageId() {
//        return imageId;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setAmount(ExpenseAmount amount) {
        this.amount = amount;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }

    @Override
    public int compareTo(ExpenseItem expenseItem) {
        if (getDate() == null || expenseItem.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(expenseItem.getDate());
    }
}
