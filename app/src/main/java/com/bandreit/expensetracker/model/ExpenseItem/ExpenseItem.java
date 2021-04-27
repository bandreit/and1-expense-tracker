package com.bandreit.expensetracker.model.ExpenseItem;

import java.util.Date;

public class ExpenseItem {
    private final String title;
    private final String category;
    private final Date date;
    private final ExpenseAmount amount;
    private final ExpenseType type;
    private final int imageId;

    public ExpenseItem(String title, String category, Date date, ExpenseAmount amount, ExpenseType type, int imageId) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public ExpenseAmount getAmount() {
        return amount;
    }

    public ExpenseType getType() {
        return type;
    }

    public int getImageId() {
        return imageId;
    }
}
