package tests.model;

import model.TaxBenefit;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxBenefitTest {

    @Test
    void testNoArgConstructor() {
        TaxBenefit benefit = new TaxBenefit();
        assertNotNull(benefit.getId());
        assertNull(benefit.getName());
        assertNull(benefit.getAmount());
        assertEquals(0, benefit.getValidFromYear());
        assertEquals(0, benefit.getValidToYear());
        assertFalse(benefit.isPercent());
    }

    @Test
    void testAllArgsConstructor() {
        String name = "Child Support";
        BigDecimal amount = new BigDecimal("1000.00");
        int from = 2020;
        int to = 2030;

        TaxBenefit benefit = new TaxBenefit(name, amount, true, from, to);

        assertNotNull(benefit.getId());
        assertEquals(name, benefit.getDescription());
        assertEquals(amount, benefit.getAmount());
        assertTrue(benefit.isPercent());
        assertEquals(from, benefit.getValidFromYear());
        assertEquals(to, benefit.getValidToYear());
    }

    @Test
    void testConstructorWithNullAmount() {
        TaxBenefit benefit = new TaxBenefit("Test", null, false, 2020, 2021);
        assertEquals(BigDecimal.ZERO, benefit.getAmount());
    }

    @Test
    void testIsApplicableTrue() {
        TaxBenefit benefit = new TaxBenefit("Test", BigDecimal.TEN, false, 2020, 2025);

        assertTrue(benefit.isApplicable(2020));
        assertTrue(benefit.isApplicable(2022));
        assertTrue(benefit.isApplicable(2025));
    }

    @Test
    void testIsApplicableFalse() {
        TaxBenefit benefit = new TaxBenefit("Test", BigDecimal.TEN, false, 2020, 2025);

        assertFalse(benefit.isApplicable(2019));
        assertFalse(benefit.isApplicable(2026));
    }

    @Test
    void testApplyBenefitFixedAmount() {
        BigDecimal benefitAmount = new BigDecimal("500.00");
        TaxBenefit benefit = new TaxBenefit("Fixed", benefitAmount, false, 2020, 2030);

        BigDecimal taxableBase = new BigDecimal("10000.00");
        BigDecimal result = benefit.applyBenefit(taxableBase);

        assertEquals(benefitAmount, result);
    }

    @Test
    void testApplyBenefitPercentage() {
        BigDecimal percent = new BigDecimal("10.00");
        TaxBenefit benefit = new TaxBenefit("Percent", percent, true, 2020, 2030);

        BigDecimal taxableBase = new BigDecimal("5000.00");
        BigDecimal result = benefit.applyBenefit(taxableBase);


        assertEquals(0, new BigDecimal("500.00").compareTo(result));
    }

    @Test
    void testApplyBenefitWithNullBase() {
        TaxBenefit benefit = new TaxBenefit("Fixed", BigDecimal.TEN, false, 2020, 2030);
        assertEquals(BigDecimal.ZERO, benefit.applyBenefit(null));
    }

    @Test
    void testApplyBenefitWithNullAmountInObject() {

        TaxBenefit benefit = new TaxBenefit("Empty", null, false, 2020, 2030);
        assertEquals(BigDecimal.ZERO, benefit.applyBenefit(new BigDecimal("100.00")));
    }
}