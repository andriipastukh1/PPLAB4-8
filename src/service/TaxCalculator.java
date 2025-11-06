package service;

import java.math.BigDecimal;

import java.util.*;

import model.*;

public class TaxCalculator {
    private List<TaxRule> rules = new ArrayList<>();

    public List<TaxPayment> calculateAllTaxes(Person person, int year) {
        /* заглушка */



        return new ArrayList<>();
    }

    public BigDecimal calculateTaxForIncome(Income income, Person person, int year) {
        /* заглушка */



        return BigDecimal.ZERO;
    }

    public BigDecimal calculateTotalTax(Person person, int year) {
        /* заглушка */



        return BigDecimal.ZERO;
    }

    public List<TaxPayment> sortTaxesByAmount(List<TaxPayment> taxes, boolean ascending) {
        /* заглушка */



        return taxes;
    }

    public List<TaxPayment> findTaxesInRange(Person person, int year, BigDecimal min, BigDecimal max) {
        /* заглушка */



        return new ArrayList<>();
    }

    public void addTaxRule(TaxRule rule) {
        rules.add(rule);



    }

    public void removeTaxRule(String ruleId) {
        /* заглушка */

    }
}
