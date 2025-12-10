package tests.model;

import model.Income;
import model.TaxCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import model.*;
class IncomeTest {

    private static class ConcreteIncome extends Income {
        public ConcreteIncome() {
            super();
        }

        public ConcreteIncome(BigDecimal amount, LocalDate date, String note) {
            super(amount, date, note);
        }

        @Override
        public TaxCategory getTaxCategory() {
            return TaxCategory.INCOME_TAX;
        }
    }

    @Test
    void testNoArgConstructor() {
        Income income = new ConcreteIncome();

        assertNotNull(income.getId());
        assertFalse(income.getId().isBlank());
        assertNull(income.getAmount());
        assertNull(income.getDate());
        assertNull(income.getNote());
    }

    @Test
    void testAllArgsConstructor() {
        BigDecimal amount = new BigDecimal("500.00");
        LocalDate date = LocalDate.of(2025, 12, 1);
        String note = "Bonus";

        Income income = new ConcreteIncome(amount, date, note);

        assertNotNull(income.getId());
        assertEquals(amount, income.getAmount());
        assertEquals(date, income.getDate());
        assertEquals(note, income.getNote());
    }

    @Test
    void testConstructorWithNullDateDefaultsToNow() {
        BigDecimal amount = new BigDecimal("100.00");
        String note = "Test";

        Income income = new ConcreteIncome(amount, null, note);

        assertNotNull(income.getDate());
        assertEquals(LocalDate.now(), income.getDate());
        assertEquals(amount, income.getAmount());
    }

    @Test
    void testIsAnnualTrue() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        Income income = new ConcreteIncome(BigDecimal.TEN, date, "Note");

        assertTrue(income.isAnnual(2024));
    }

    @Test
    void testIsAnnualFalse() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        Income income = new ConcreteIncome(BigDecimal.TEN, date, "Note");

        assertFalse(income.isAnnual(2025));
    }

    @Test
    void testIsAnnualWithNullDate() {
        Income income = new ConcreteIncome();
        assertFalse(income.isAnnual(2025));
    }

    @Test
    void testPolymorphicBehavior() {
        Income income = new ConcreteIncome();
        assertEquals(TaxCategory.INCOME_TAX, income.getTaxCategory());
    }
}