package tests.model;

import model.TaxCategory;
import model.TaxRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxRuleTest {

    @Test
    void testNoArgConstructor() {
        TaxRule rule = new TaxRule();

        assertNull(rule.getName());
        assertNull(rule.getTaxCategory());
    }

    @Test
    void testFullConstructor() {
        String name = "Luxury Tax";
        BigDecimal rate = new BigDecimal("0.05");
        BigDecimal threshold = new BigDecimal("10000.00");
        TaxCategory category = TaxCategory.PROPERTY_TAX;

        TaxRule rule = new TaxRule(name, rate, threshold, category);

        assertEquals(name, rule.getName());
        assertEquals(category, rule.getTaxCategory());
    }

    @Test
    void testApplyRuleStandard() {
        TaxRule rule = new TaxRule("Rule", new BigDecimal("0.20"), BigDecimal.ZERO, TaxCategory.INCOME_TAX);
        BigDecimal income = new BigDecimal("1000.00");

        BigDecimal tax = rule.applyRule(income);

        assertEquals(0, new BigDecimal("200.00").compareTo(tax));
    }

    @Test
    void testApplyRuleWithNullAmount() {
        TaxRule rule = new TaxRule("Rule", new BigDecimal("0.20"), BigDecimal.ZERO, TaxCategory.INCOME_TAX);

        BigDecimal tax = rule.applyRule(null);

        assertEquals(BigDecimal.ZERO, tax);
    }

    @Test
    void testApplyRuleWithNullRate() {
        TaxRule rule = new TaxRule("Rule", null, BigDecimal.ZERO, TaxCategory.INCOME_TAX);

        BigDecimal tax = rule.applyRule(new BigDecimal("1000.00"));

        assertEquals(BigDecimal.ZERO, tax);
    }

    @Test
    void testAppliesToAboveThreshold() {
        BigDecimal threshold = new BigDecimal("5000.00");
        TaxRule rule = new TaxRule("Rule", BigDecimal.ONE, threshold, TaxCategory.INCOME_TAX);

        assertTrue(rule.appliesTo(new BigDecimal("5000.01")));
    }

    @Test
    void testAppliesToBelowThreshold() {
        BigDecimal threshold = new BigDecimal("5000.00");
        TaxRule rule = new TaxRule("Rule", BigDecimal.ONE, threshold, TaxCategory.INCOME_TAX);

        assertFalse(rule.appliesTo(new BigDecimal("4000.00")));
    }

    @Test
    void testAppliesToExactThreshold() {
        BigDecimal threshold = new BigDecimal("5000.00");
        TaxRule rule = new TaxRule("Rule", BigDecimal.ONE, threshold, TaxCategory.INCOME_TAX);

        assertFalse(rule.appliesTo(new BigDecimal("5000.00")));
    }

    @Test
    void testAppliesToNullThreshold() {
        TaxRule rule = new TaxRule("Rule", BigDecimal.ONE, null, TaxCategory.INCOME_TAX);

        assertTrue(rule.appliesTo(new BigDecimal("100.00")));
    }
}