package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.interfaces.IncomeSource;

public abstract class Income {
    protected String id;
    protected IncomeSource source;
    protected LocalDate date;
    protected BigDecimal amount;
    protected String note;



    public LocalDate getDate() { return date; }

    public String getId() { return id; }

    public IncomeSource getSource() { return source; }


    public boolean isAnnual(int year) {
        return date.getYear() == year;
    }


    public abstract TaxCategory getTaxCategory();
}
