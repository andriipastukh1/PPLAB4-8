package tests.service;


import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorTest {

    private TaxCalculator calculator;
    private Person person;
    private int currentYear;

    @BeforeEach
    void setUp() {
        calculator = new TaxCalculator();

        calculator.updateRate(TaxCategory.INCOME_TAX, new BigDecimal("0.18"));
        calculator.updateRate(TaxCategory.MILITARY_TAX, new BigDecimal("0.015"));
        calculator.updateRate(TaxCategory.GIFT_TAX, new BigDecimal("0.05"));
        calculator.updateRate(TaxCategory.FOREIGN_INCOME_TAX, new BigDecimal("0.20"));
        calculator.updateRate(TaxCategory.ROYALTY_TAX, new BigDecimal("0.10"));
        calculator.updateRate(TaxCategory.CAPITAL_GAINS, new BigDecimal("0.05"));
        calculator.updateRate(TaxCategory.PROPERTY_TAX, new BigDecimal("0.01"));

        person = new Person("Test", "User");
        currentYear = LocalDate.now().getYear();
    }

    @Test
    void testCalculateJobIncomeTaxStandard() {
        Income income = new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Company", "Salary");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal expectedIncomeTax = new BigDecimal("1000.00").multiply(new BigDecimal("0.18"));
        BigDecimal expectedMilitary = new BigDecimal("1000.00").multiply(new BigDecimal("0.015"));
        BigDecimal expectedTotal = expectedIncomeTax.add(expectedMilitary);

        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testCalculateJobIncomeTaxWithBenefit() {
        Income income = new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Company", "Salary");
        TaxBenefit benefit = new TaxBenefit("Child", new BigDecimal("200.00"), false, currentYear, currentYear);
        person.addBenefit(benefit);

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal taxableBase = new BigDecimal("800.00");
        BigDecimal expectedIncomeTax = taxableBase.multiply(new BigDecimal("0.18"));
        BigDecimal expectedMilitary = new BigDecimal("1000.00").multiply(new BigDecimal("0.015"));
        BigDecimal expectedTotal = expectedIncomeTax.add(expectedMilitary);

        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testCalculateJobIncomeTaxWithBenefitExceedingIncome() {
        Income income = new JobIncome(new BigDecimal("100.00"), LocalDate.now(), "Company", "Salary");
        TaxBenefit benefit = new TaxBenefit("Big Benefit", new BigDecimal("500.00"), false, currentYear, currentYear);
        person.addBenefit(benefit);

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal expectedIncomeTax = BigDecimal.ZERO;
        BigDecimal expectedMilitary = new BigDecimal("100.00").multiply(new BigDecimal("0.015"));
        BigDecimal expectedTotal = expectedIncomeTax.add(expectedMilitary);

        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testCalculateGiftTax() {
        Income income = new GiftIncome(new BigDecimal("5000.00"), LocalDate.now(), "Friend", true, "Gift");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal expected = new BigDecimal("5000.00").multiply(new BigDecimal("0.05"));

        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void testCalculateForeignIncomeTax() {
        Income income = new ForeignIncome(new BigDecimal("2000.00"), LocalDate.now(), "USA", "Work");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal expected = new BigDecimal("2000.00").multiply(new BigDecimal("0.20"));

        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void testCalculateRoyaltyTax() {
        Income income = new RoyaltyIncome(new BigDecimal("10000.00"), LocalDate.now(), "Book", "Royalty");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal expected = new BigDecimal("10000.00").multiply(new BigDecimal("0.10"));

        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void testCalculateCapitalGainsTaxPositive() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("10000.00"));

        Income income = new PropertySaleIncome(property, new BigDecimal("15000.00"), LocalDate.now(), "Sale");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        BigDecimal gain = new BigDecimal("5000.00");
        BigDecimal expected = gain.multiply(new BigDecimal("0.05"));

        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void testCalculateCapitalGainsTaxLoss() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("20000.00"));

        Income income = new PropertySaleIncome(property, new BigDecimal("15000.00"), LocalDate.now(), "Sale");

        BigDecimal result = calculator.calculateTaxForIncome(income, person, currentYear);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testCalculatePropertyAssetTax() {
        Property property = new Property();
        property.setDescription("House");
        property.setEstimatedValue(new BigDecimal("100000.00"));
        person.addProperty(property);

        List<TaxPayment> taxes = calculator.calculateAllTaxes(person, currentYear);

        boolean foundPropertyTax = false;
        for (TaxPayment tp : taxes) {
            if (tp.getTaxCategory() == TaxCategory.PROPERTY_TAX) {
                foundPropertyTax = true;
                BigDecimal expected = new BigDecimal("100000.00").multiply(new BigDecimal("0.01"));
                assertEquals(0, expected.compareTo(tp.getAmount()));
            }
        }
        assertTrue(foundPropertyTax);
    }

    @Test
    void testCalculateTotalTax() {
        person.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "A", "n"));
        person.addIncome(new GiftIncome(new BigDecimal("1000.00"), LocalDate.now(), "B", true, "n"));

        BigDecimal jobTax = new BigDecimal("195.00");
        BigDecimal giftTax = new BigDecimal("50.00");
        BigDecimal expectedTotal = jobTax.add(giftTax);

        BigDecimal actualTotal = calculator.calculateTotalTax(person, currentYear);

        assertEquals(0, expectedTotal.compareTo(actualTotal));
    }

    @Test
    void testIgnoreIncomeFromOtherYears() {
        int nextYear = currentYear + 1;
        person.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.of(nextYear, 1, 1), "A", "n"));

        BigDecimal total = calculator.calculateTotalTax(person, currentYear);

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testSortTaxesAscending() {
        List<TaxPayment> list = new ArrayList<>();
        list.add(new TaxPayment(new BigDecimal("100.00"), currentYear, "Medium", TaxCategory.INCOME_TAX));
        list.add(new TaxPayment(new BigDecimal("10.00"), currentYear, "Small", TaxCategory.INCOME_TAX));
        list.add(new TaxPayment(new BigDecimal("500.00"), currentYear, "Large", TaxCategory.INCOME_TAX));

        List<TaxPayment> sorted = calculator.sortTaxesByAmount(list, true);

        assertEquals(new BigDecimal("10.00"), sorted.get(0).getAmount());
        assertEquals(new BigDecimal("100.00"), sorted.get(1).getAmount());
        assertEquals(new BigDecimal("500.00"), sorted.get(2).getAmount());
    }

    @Test
    void testSortTaxesDescending() {
        List<TaxPayment> list = new ArrayList<>();
        list.add(new TaxPayment(new BigDecimal("100.00"), currentYear, "Medium", TaxCategory.INCOME_TAX));
        list.add(new TaxPayment(new BigDecimal("10.00"), currentYear, "Small", TaxCategory.INCOME_TAX));
        list.add(new TaxPayment(new BigDecimal("500.00"), currentYear, "Large", TaxCategory.INCOME_TAX));

        List<TaxPayment> sorted = calculator.sortTaxesByAmount(list, false);

        assertEquals(new BigDecimal("500.00"), sorted.get(0).getAmount());
        assertEquals(new BigDecimal("100.00"), sorted.get(1).getAmount());
        assertEquals(new BigDecimal("10.00"), sorted.get(2).getAmount());
    }

    @Test
    void testFindTaxesInRange() {
        person.addTaxPayment(new TaxPayment(new BigDecimal("10.00"), currentYear, "Low", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(new BigDecimal("50.00"), currentYear, "Mid", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(new BigDecimal("100.00"), currentYear, "High", TaxCategory.INCOME_TAX));


        Person p2 = new Person("Search", "Test");
        // Tax 19.5
        p2.addIncome(new JobIncome(new BigDecimal("100.00"), LocalDate.now(), "A", "n"));
        // Tax 50.0
        p2.addIncome(new GiftIncome(new BigDecimal("1000.00"), LocalDate.now(), "B", true, "n"));

        List<TaxPayment> result = calculator.findTaxesInRange(p2, currentYear, new BigDecimal("40.00"), new BigDecimal("60.00"));

        assertEquals(1, result.size());
        assertEquals(0, new BigDecimal("50.00").compareTo(result.get(0).getAmount()));
    }
}