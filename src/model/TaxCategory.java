package model;

public enum TaxCategory {
    MILITARY_TAX("Military tax", 0.015),
    INCOME_TAX("Income tax (PIT)", 0.18),
    PROPERTY_TAX("Property tax", 0.05),
    GIFT_TAX("Gift tax", 0.10),
    FOREIGN_INCOME_TAX("Foreign income tax", 0.20),
    CAPITAL_GAINS("Capital gains tax", 0.10);

    private final String description;


    private final double defaultRate;

    TaxCategory(String description, double defaultRate) {


        this.description = description;


        this.defaultRate = defaultRate;




    }

    public String getDescription() { return description; }
    public double getDefaultRate() { return defaultRate; }
}
