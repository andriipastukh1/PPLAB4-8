package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Income implements Serializable {
    protected String id;
    protected LocalDate date;


    protected BigDecimal amount;


    protected String note;

    public Income() { this.id = java.util.UUID.randomUUID().toString(); }

    public Income(BigDecimal amount, LocalDate date, String note) {
        this();


        this.amount = amount;


        this.date = date != null ? date : LocalDate.now();


        this.note = note;
    }

    public String getId() { return id; }


    public LocalDate getDate() { return date; }


    public BigDecimal getAmount() { return amount; }


    public String getNote() { return note; }


    public boolean isAnnual(int year) {
        return date != null && date.getYear() == year;
    }



    public abstract TaxCategory getTaxCategory();
}
