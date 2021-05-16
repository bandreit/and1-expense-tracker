package com.bandreit.expensetracker.model.transactions;

import java.util.Calendar;

public class SectionOrRow {
    private TransactionItem row;
    private Calendar section;
    private boolean isRow;

    public static SectionOrRow createRow(TransactionItem row) {
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

    public TransactionItem getRow() {
        return row;
    }

    public Calendar getSection() {
        return section;
    }

    public boolean isRow() {
        return isRow;
    }
}
