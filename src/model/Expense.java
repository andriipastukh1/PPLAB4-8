package model;

import java.math.BigDecimal;
import java.time.LocalDate;



public class Expense {
    private String id;
    private String description;


    private BigDecimal amount;

    private LocalDate date;



    public boolean isDeductibleByRule(TaxRule rule) {
        /* заглушка */


        return false;
    }
}
