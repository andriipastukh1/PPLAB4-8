package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Property {
    private String id;
    private String description;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private BigDecimal currentEstimatedValue;

    public BigDecimal getEstimatedValue() { return currentEstimatedValue; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }





    public BigDecimal getCapitalGain(BigDecimal salePrice) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    public void updateEstimatedValue(BigDecimal newValue) {
        /* заглушка */

    }
}
