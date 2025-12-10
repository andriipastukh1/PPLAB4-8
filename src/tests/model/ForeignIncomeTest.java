package tests.model;

import model.ForeignIncome;
import model.TaxCategory;
import org.junit.jupiter.api.Test;
import model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ForeignIncomeTest {

    @Test
    void testNoArgConstructor() {
        ForeignIncome income = new ForeignIncome();

        assertNotNull(income.getId());
        assertNull(income.getAmount());
        assertNull(income.getDate());
        assertNull(income.getOriginCountry());
        assertNull(income.getNote());
    }

    @Test
    void testFullConstructor() {
        BigDecimal amount = new BigDecimal("1500.00");
        LocalDate date = LocalDate.of(2025, 5, 20);
        String country = "USA";
        String note = "Freelance Project";

        ForeignIncome income = new ForeignIncome(amount, date, country, note);

        assertNotNull(income.getId());
        assertEquals(amount, income.getAmount());
        assertEquals(date, income.getDate());
        assertEquals(country, income.getOriginCountry());
        assertEquals(note, income.getNote());
    }

    @Test
    void testGetTaxCategory() {
        ForeignIncome income = new ForeignIncome();
        assertEquals(TaxCategory.FOREIGN_INCOME_TAX, income.getTaxCategory());
    }

    @Test
    void testInheritedMethods() {

        LocalDate date = LocalDate.of(2025, 1, 1);
        ForeignIncome income = new ForeignIncome(BigDecimal.TEN, date, "Pol", "n");

        assertTrue(income.isAnnual(2025));
        assertFalse(income.isAnnual(2024));
    }
}