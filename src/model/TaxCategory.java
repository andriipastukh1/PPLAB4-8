package model;

public enum TaxCategory {
    //перевірити чи податки правьні і змінити


    MILITARY_TAX("Military Tax", 0.015),
    INCOME_TAX("Income Tax", 0.18),
    PROPERTY_TAX("Property Tax", 0.05),
    GIFT_TAX("Gift Tax", 0.10),
    FOREIGN_INCOME_TAX("Foreign Income Tax", 0.20);

    private final String description;
    private final double rate;

    TaxCategory(String description, double rate) {
        this.description = description;
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public double getRate() {
        return rate;
    }
}
