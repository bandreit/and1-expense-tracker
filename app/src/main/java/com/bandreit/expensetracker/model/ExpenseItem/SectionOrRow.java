package com.bandreit.expensetracker.model.ExpenseItem;

import java.util.Calendar;
import java.util.Date;

public class SectionOrRow {
    private ExpenseItem row;
    private Calendar section;
    private boolean isRow;

    public static SectionOrRow createRow(ExpenseItem row) {
        SectionOrRow ret = new SectionOrRow();
        ret.row = row;
        ret.isRow = true;
        return ret;
    }

    public static SectionOrRow createSection(Calendar section) {
        SectionOrRow ret = new SectionOrRow();
        ret.section = section;
        ret.isRow = false;
        return ret;
    }

    public ExpenseItem getRow() {
        return row;
    }

    public Calendar getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }
}
