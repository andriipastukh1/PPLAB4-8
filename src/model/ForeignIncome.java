package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ForeignIncome extends Income {


    private String originCountry;



    public ForeignIncome() { super(); }

    public ForeignIncome(BigDecimal amount, LocalDate date, String originCountry, String note) {
        super(amount, date, note);


        this.originCountry = originCountry;
    }

    public String getOriginCountry() { return originCountry; }

    @Override



    public TaxCategory getTaxCategory() {
        return TaxCategory.FOREIGN_INCOME_TAX;
    }
}
