package model;

public class GiftIncome extends Income {
    private String giver;
    private boolean monetary;

    public String getGiver() { return giver; }
    public boolean isMonetary() { return monetary; }

    @Override
    public TaxCategory getTaxCategory() {
        /* заглушка */




        return null;
    }
}
