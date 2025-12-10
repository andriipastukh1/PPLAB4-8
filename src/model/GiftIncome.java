package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GiftIncome extends Income {


    private String giver;



    private boolean monetary;

    public GiftIncome() {

        super();


    }

    public GiftIncome(BigDecimal amount, LocalDate date, String giver, boolean monetary, String note) {
        super(amount, date, note);
        this.giver = giver;

        this.monetary = monetary;
    }

    public String getGiver() { return giver; }



    public boolean isMonetary() { return monetary; }

    @Override
    public TaxCategory getTaxCategory() {


        return TaxCategory.GIFT_TAX;
    }
}
