package model;

import java.math.BigDecimal;

public class PropertySaleIncome extends Income {
    private Property propertySold;

    public Property getPropertySold() { return propertySold; }

    public BigDecimal calculateCapitalGain(BigDecimal purchasePrice, BigDecimal salePrice) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    @Override
    public TaxCategory getTaxCategory() {
        /* заглушка */

        return null;
    }
}
