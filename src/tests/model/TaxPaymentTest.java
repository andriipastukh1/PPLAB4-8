package tests.model;

import model.TaxCategory;
import model.TaxPayment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaxPaymentTest {

    @Test
    void testNoArgConstructor() {
        TaxPayment payment = new TaxPayment();

        assertNotNull(payment);
        assertFalse(payment.isPaid());
        assertNotNull(payment.getDateCalculated());
    }

    @Test
    void testFullConstructor() {
        BigDecimal amount = new BigDecimal("150.00");
        int year = 2025;
        String reason = "Income Tax";
        TaxCategory category = TaxCategory.INCOME_TAX;

        TaxPayment payment = new TaxPayment(amount, year, reason, category);

        assertEquals(amount, payment.getAmount());
        assertEquals(year, payment.getTaxYear());
        assertEquals(reason, payment.getReason());
        assertEquals(category, payment.getTaxCategory());
        assertFalse(payment.isPaid());
        assertNotNull(payment.getDateCalculated());
    }

    @Test
    void testConstructorWithNullAmount() {
        TaxPayment payment = new TaxPayment(null, 2025, "Test", TaxCategory.GIFT_TAX);

        assertNotNull(payment.getAmount());
        assertEquals(BigDecimal.ZERO, payment.getAmount());
    }

    @Test
    void testMarkAsPaid() {
        TaxPayment payment = new TaxPayment(BigDecimal.TEN, 2025, "Test", TaxCategory.INCOME_TAX);

        assertFalse(payment.isPaid());

        LocalDate beforePay = LocalDate.now();
        payment.markAsPaid();

        assertTrue(payment.isPaid());

        LocalDate dateCalculated = payment.getDateCalculated();
        assertNotNull(dateCalculated);
        assertTrue(dateCalculated.isEqual(LocalDate.now()) || dateCalculated.isAfter(beforePay.minusDays(1)));
    }

    @Test
    void testToString() {
        TaxPayment payment = new TaxPayment(new BigDecimal("99.99"), 2024, "Test Reason", TaxCategory.MILITARY_TAX);
        String result = payment.toString();

        assertTrue(result.contains("TaxPayment"));
        assertTrue(result.contains("MILITARY_TAX"));
        assertTrue(result.contains("Test Reason"));
        assertTrue(result.contains("99.99"));
        assertTrue(result.contains("2024"));
        assertTrue(result.contains("paid=false"));
    }
}