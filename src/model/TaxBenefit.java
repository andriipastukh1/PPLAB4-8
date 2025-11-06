package model;

import java.math.BigDecimal;

public class TaxBenefit {
    private String id;
    private String name;
    private BigDecimal amount;
    private int validFromYear;
    private int validToYear;
    private boolean isPercent;

    public BigDecimal applyBenefit(BigDecimal taxableBase) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    public boolean isApplicable(Person person, int year) {
        /* заглушка */

        return false;
    }

    public String getDescription() { return name; }
}
