package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense implements Serializable {



    private String id = java.util.UUID.randomUUID().toString();


    private String description;
    private BigDecimal amount;


    private LocalDate date;



    public Expense() {}
    public Expense(String desc, BigDecimal amount, LocalDate date) {


        this.description = desc; this.amount = amount; this.date = date;
    }
    public BigDecimal getAmount() { return amount; }


    public String getDescription() { return description; }
}
