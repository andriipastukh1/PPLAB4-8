package tests.model;

import model.JobIncome;
import model.TaxCategory;
import org.junit.jupiter.api.Test;
import model.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobIncomeTest {

    @Test
    void testNoArgConstructor() {
        JobIncome income = new JobIncome();

        assertNotNull(income.getId());
        assertNull(income.getAmount());
        assertNull(income.getDate());
        assertNull(income.getEmployer());
        assertNull(income.getNote());
    }

    @Test
    void testFullConstructor() {
        BigDecimal amount = new BigDecimal("25000.00");
        LocalDate date = LocalDate.of(2025, 4, 15);
        String employer = "Tech Corp";
        String note = "Salary April";

        JobIncome income = new JobIncome(amount, date, employer, note);

        assertNotNull(income.getId());
        assertEquals(amount, income.getAmount());
        assertEquals(date, income.getDate());
        assertEquals(employer, income.getEmployer());
        assertEquals(note, income.getNote());
    }

    @Test
    void testCalculateNetIncomeNoExpenses() {
        BigDecimal amount = new BigDecimal("1000.00");
        JobIncome income = new JobIncome(amount, LocalDate.now(), "Comp", "n");

        assertEquals(amount, income.calculateNetIncome());
    }

    @Test
    void testCalculateNetIncomeWithExpenses() throws Exception {
        BigDecimal amount = new BigDecimal("1000.00");
        JobIncome income = new JobIncome(amount, LocalDate.now(), "Comp", "n");

        Field field = JobIncome.class.getDeclaredField("deductibleExpenses");
        field.setAccessible(true);
        field.set(income, List.of(new BigDecimal("100.00"), new BigDecimal("50.00")));

        assertEquals(new BigDecimal("850.00"), income.calculateNetIncome());
    }

    @Test
    void testCalculateNetIncomeNegativeResult() throws Exception {
        BigDecimal amount = new BigDecimal("100.00");
        JobIncome income = new JobIncome(amount, LocalDate.now(), "Comp", "n");

        Field field = JobIncome.class.getDeclaredField("deductibleExpenses");
        field.setAccessible(true);
        field.set(income, List.of(new BigDecimal("200.00")));

        assertEquals(BigDecimal.ZERO, income.calculateNetIncome());
    }

    @Test
    void testCalculateNetIncomeNullAmount() {
        JobIncome income = new JobIncome();
        assertEquals(BigDecimal.ZERO, income.calculateNetIncome());
    }

    @Test
    void testGetTaxCategory() {
        JobIncome income = new JobIncome();
        assertEquals(TaxCategory.INCOME_TAX, income.getTaxCategory());
    }
}