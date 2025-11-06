package model;

import java.math.BigDecimal;

import model.interfaces.IncomeSource;

public class TaxRule {
    private String id;
    private String name;
    private BigDecimal rate;
    private BigDecimal threshold;
    private TaxCategory tax_cat;



    public BigDecimal applyRule(BigDecimal taxableAmount) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    public boolean appliesTo(IncomeSource source) {
        /* заглушка */

        return false;
    }

    public String getName() { return name; }
}
