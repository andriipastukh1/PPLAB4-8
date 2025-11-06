package model.interfaces;

import model.TaxRule;


public interface IncomeSource {
    String getName();
    boolean isTaxable();
    TaxRule getApplicableTaxRule();
}
