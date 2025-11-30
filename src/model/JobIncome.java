package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class JobIncome extends Income {
    private String employer;



    private List<BigDecimal> deductibleExpenses;

    public JobIncome() { super(); }

    public JobIncome(BigDecimal amount, LocalDate date, String employer, String note) {
        super(amount, date, note);


        this.employer = employer;


    }

    public String getEmployer() { return employer; }

    public BigDecimal calculateNetIncome() {


        BigDecimal gross = amount == null ? BigDecimal.ZERO : amount;


        if (deductibleExpenses != null) {
            for (BigDecimal e : deductibleExpenses) if (e != null) gross = gross.subtract(e);


        }


        if (gross.compareTo(BigDecimal.ZERO) < 0) gross = BigDecimal.ZERO;
        return gross;
    }

    @Override
    public TaxCategory getTaxCategory() {


        return TaxCategory.INCOME_TAX;
    }
}
