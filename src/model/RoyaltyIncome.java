package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RoyaltyIncome extends Income {
    private String intellectualProperty;

    public RoyaltyIncome() { super(); }

    public RoyaltyIncome(BigDecimal amount, LocalDate date, String intellectualProperty, String note) {
        super(amount, date, note);
        this.intellectualProperty = intellectualProperty;
    }

    public String getIntellectualProperty() {
        return intellectualProperty;
    }

    @Override
    public TaxCategory getTaxCategory() {
        return TaxCategory.ROYALTY_TAX;
    }
}