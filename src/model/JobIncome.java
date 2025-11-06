package model;

import java.math.BigDecimal;
import java.util.List;

import service.TaxCalculator;

public class JobIncome extends Income {
    private String employer;

    public String getEmployer() { return employer; }
    public void setEmployer(String emp) { this.employer = emp; }

    public BigDecimal calculateNetIncome(BigDecimal gross, List<Expense> expenses, TaxCalculator calc) {


        /* заглушка */



        return BigDecimal.ZERO;
    }

    @Override
    public TaxCategory getTaxCategory() {



        /* заглушка */

        return null;
    }
}
