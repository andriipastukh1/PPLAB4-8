package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Property implements Serializable {
    private String id = java.util.UUID.randomUUID().toString();
    private String description;
    private BigDecimal purchasePrice;


    private LocalDate purchaseDate;


    private BigDecimal currentEstimatedValue;

    public String getId() { return id; }
    public String getDescription() { return description; }




    public void setDescription(String d) { this.description = d; }


    public void setPurchasePrice(BigDecimal p) { this.purchasePrice = p; }


    public BigDecimal getPurchasePrice() { return purchasePrice; }


    public void setPurchaseDate(LocalDate d) { this.purchaseDate = d; }
    public BigDecimal getEstimatedValue() { return currentEstimatedValue; }
    public void setEstimatedValue(BigDecimal v) { this.currentEstimatedValue = v; }



    public BigDecimal getCapitalGain(BigDecimal salePrice) {
        if (salePrice == null || purchasePrice == null) return BigDecimal.ZERO;
        return salePrice.subtract(purchasePrice);
    }



    @Override
    public String toString() {
        return "Property[" + id + " " + description + "]";
    }
}
