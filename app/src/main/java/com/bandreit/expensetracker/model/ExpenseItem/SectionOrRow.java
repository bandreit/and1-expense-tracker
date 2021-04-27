package com.bandreit.expensetracker.model.ExpenseItem;

public class SectionOrRow {
    private ExpenseItem row;
    private String section;
    private boolean isRow;

    public static SectionOrRow createRow(ExpenseItem row) {
        SectionOrRow ret = new SectionOrRow();
        ret.row = row;
        ret.isRow = true;
        return ret;
    }

    public static SectionOrRow createSection(String section) {
        SectionOrRow ret = new SectionOrRow();
        ret.section = section;
        ret.isRow = false;
        return ret;
    }

    public ExpenseItem getRow() {
        return row;
    }

    public String getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }
}
