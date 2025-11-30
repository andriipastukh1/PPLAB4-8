package model;

import java.io.Serializable;


import java.math.BigDecimal;
import java.util.UUID;

public class TaxBenefit implements Serializable {




    private static final long serialVersionUID = 1L;



    private String id = UUID.randomUUID().toString();
    private String name;
    private BigDecimal amount;

       // if isPercent==true -> amount is percent value (e.g. 10 for 10%)
    private int validFromYear;
    private int validToYear;


    private boolean isPercent;

    public TaxBenefit() {}

    public TaxBenefit(String name, BigDecimal amount, boolean isPercent, int validFromYear, int validToYear) {
        this.name = name;
        this.amount = amount == null ? BigDecimal.ZERO : amount;


        this.isPercent = isPercent;


        this.validFromYear = validFromYear;


        this.validToYear = validToYear;
    }

    public String getId() { return id; }


    public String getDescription() { return name; }


    public String getName() { return name; }


    public BigDecimal getAmount() { return amount; }
    public boolean isPercent() { return isPercent; }





    public int getValidFromYear() { return validFromYear; }
    public int getValidToYear() { return validToYear; }


    public BigDecimal applyBenefit(BigDecimal taxableBase) {
        if (taxableBase == null) return BigDecimal.ZERO;


        if (isPercent) {


            return taxableBase.multiply(amount).divide(new BigDecimal("100"));
        } else {


            return amount == null ? BigDecimal.ZERO : amount;
        }
    }


    public boolean isApplicable(int year) {




        return year >= validFromYear && year <= validToYear;






    }
}
