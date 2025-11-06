package model;

import java.math.BigDecimal;


public class ForeignIncome extends Income {
    private String originCountry;


    public String getOriginCountry() { return originCountry; }


    @Override
    public TaxCategory getTaxCategory() {
        /* заглушка */


        
        return null;
    }
}
