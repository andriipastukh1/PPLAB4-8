package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TaxRule implements Serializable {
    private String id = java.util.UUID.randomUUID().toString();




    private String name;




    private BigDecimal rate;




    private BigDecimal threshold;
    private TaxCategory taxCategory;

    public TaxRule() {}
    public TaxRule(String name, BigDecimal rate, BigDecimal threshold, TaxCategory cat) {




     this.name = name; this.rate = rate; this.threshold = threshold; this.taxCategory = cat;


    }

    public BigDecimal applyRule(BigDecimal taxableAmount) {


        if (taxableAmount == null || rate == null) return BigDecimal.ZERO;




        return taxableAmount.multiply(rate);
    }

    public boolean appliesTo(BigDecimal taxable) {


        if (threshold == null) return true;




        return taxable.compareTo(threshold) > 0;
    }

    public String getName() {



    return name; }
    public TaxCategory getTaxCategory() { return taxCategory; }
}
