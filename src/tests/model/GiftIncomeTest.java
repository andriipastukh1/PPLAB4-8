package tests.model;

import model.GiftIncome;
import model.TaxCategory;
import org.junit.jupiter.api.Test;
import model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GiftIncomeTest {

    @Test
    void testNoArgConstructor() {
        GiftIncome income = new GiftIncome();

        assertNotNull(income.getId());
        assertNull(income.getAmount());
        assertNull(income.getDate());
        assertNull(income.getGiver());
        assertFalse(income.isMonetary());
    }

    @Test
    void testFullConstructorMonetary() {
        BigDecimal amount = new BigDecimal("5000.00");
        LocalDate date = LocalDate.of(2025, 12, 25);
        String giver = "Grandmother";
        String note = "Christmas";

        GiftIncome income = new GiftIncome(amount, date, giver, true, note);

        assertNotNull(income.getId());
        assertEquals(amount, income.getAmount());
        assertEquals(date, income.getDate());
        assertEquals(giver, income.getGiver());
        assertTrue(income.isMonetary());
        assertEquals(note, income.getNote());
    }

    @Test
    void testFullConstructorNonMonetary() {
        BigDecimal value = new BigDecimal("15000.00");
        LocalDate date = LocalDate.now();
        String giver = "Friend";
        String note = "Car";

        GiftIncome income = new GiftIncome(value, date, giver, false, note);

        assertEquals(giver, income.getGiver());
        assertFalse(income.isMonetary());
        assertEquals(value, income.getAmount());
    }

    @Test
    void testGetTaxCategory() {
        GiftIncome income = new GiftIncome();
        assertEquals(TaxCategory.GIFT_TAX, income.getTaxCategory());
    }
}