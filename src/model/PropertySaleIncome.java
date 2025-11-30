package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PropertySaleIncome extends Income {
    private Property propertySold;


    private BigDecimal salePrice;



    public PropertySaleIncome() { super(); }

    public PropertySaleIncome(Property prop, BigDecimal salePrice, LocalDate date, String note) {
        super(salePrice, date, note);


        this.propertySold = prop;


        this.salePrice = salePrice;
    }

    public Property getPropertySold() {

    return propertySold;

    }
    public BigDecimal getSalePrice() {

    return salePrice;

    }



    public BigDecimal calculateCapitalGain() {
        if (propertySold == null || salePrice == null) return BigDecimal.ZERO;
        return propertySold.getCapitalGain(salePrice);
    }

    @Override
    public TaxCategory getTaxCategory() {
        return TaxCategory.CAPITAL_GAINS;
    }
}
