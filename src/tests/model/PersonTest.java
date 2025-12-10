package tests.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("John", "Doe");
    }

    @Test
    void testConstructorAndIdentity() {
        assertNotNull(person.getId());
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("John Doe", person.getFullName());
    }

    @Test
    void testIncomeManagement() {
        Income income = new JobIncome(BigDecimal.TEN, LocalDate.now(), "Job", "Note");

        person.addIncome(income);
        assertEquals(1, person.incomes.size());

        person.removeIncome(income.getId());
        assertEquals(0, person.incomes.size());
    }

    @Test
    void testDependentManagement() {
        Dependent dependent = new Dependent("Child", LocalDate.now());

        person.addDependent(dependent);
        assertEquals(1, person.dependents.size());

        person.removeDependent("Child");
        assertEquals(0, person.dependents.size());
    }

    @Test
    void testPropertyManagement() {
        Property property = new Property();
        property.setDescription("House");

        person.addProperty(property);
        assertEquals(1, person.properties.size());

        person.removeProperty(property.getId());
        assertEquals(0, person.properties.size());
    }

    @Test
    void testBenefitManagement() {
        TaxBenefit benefit = new TaxBenefit("Benefit", BigDecimal.ONE, false, 2020, 2030);

        person.addBenefit(benefit);
        assertEquals(1, person.benefits.size());

        person.removeBenefit("Benefit");
        assertEquals(0, person.benefits.size());
    }

    @Test
    void testTaxPaymentManagement() {
        TaxPayment payment = new TaxPayment(BigDecimal.TEN, 2025, "Tax", TaxCategory.INCOME_TAX);

        person.addTaxPayment(payment);
        assertEquals(1, person.taxPayments.size());

        person.removeTaxPayment(payment);
        assertEquals(0, person.taxPayments.size());
    }

    @Test
    void testGetTotalAnnualIncome() {
        int year = 2025;
        person.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.of(year, 1, 1), "A", "n"));
        person.addIncome(new JobIncome(new BigDecimal("2000.00"), LocalDate.of(year, 5, 1), "B", "n"));
        person.addIncome(new JobIncome(new BigDecimal("5000.00"), LocalDate.of(year - 1, 1, 1), "C", "n"));

        BigDecimal total = person.getTotalAnnualIncome(year);

        assertEquals(0, new BigDecimal("3000.00").compareTo(total));
    }

    @Test
    void testGetTaxableIncomeWithBenefits() {
        int year = 2025;
        person.addIncome(new JobIncome(new BigDecimal("5000.00"), LocalDate.of(year, 1, 1), "Job", "n"));

        person.addBenefit(new TaxBenefit("Relief", new BigDecimal("1000.00"), false, year, year));

        BigDecimal taxable = person.getTaxableIncome(year);

        assertEquals(0, new BigDecimal("4000.00").compareTo(taxable));
    }

    @Test
    void testGetTaxableIncomeCannotBeNegative() {
        int year = 2025;
        person.addIncome(new JobIncome(new BigDecimal("100.00"), LocalDate.of(year, 1, 1), "Job", "n"));

        person.addBenefit(new TaxBenefit("Huge Relief", new BigDecimal("10000.00"), false, year, year));

        BigDecimal taxable = person.getTaxableIncome(year);

        assertEquals(BigDecimal.ZERO, taxable);
    }

    @Test
    void testGetTaxPaymentsForYear() {
        person.addTaxPayment(new TaxPayment(BigDecimal.TEN, 2024, "A", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(BigDecimal.TEN, 2025, "B", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(BigDecimal.TEN, 2025, "C", TaxCategory.INCOME_TAX));

        List<TaxPayment> result = person.getTaxPaymentsForYear(2025);

        assertEquals(2, result.size());
    }

    @Test
    void testGetTaxPaymentsInRange() {
        person.addTaxPayment(new TaxPayment(new BigDecimal("10.00"), 2025, "Low", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(new BigDecimal("50.00"), 2025, "Mid", TaxCategory.INCOME_TAX));
        person.addTaxPayment(new TaxPayment(new BigDecimal("100.00"), 2025, "High", TaxCategory.INCOME_TAX));

        List<TaxPayment> result = person.getTaxPaymentsInRange(new BigDecimal("20.00"), new BigDecimal("80.00"));

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("50.00"), result.get(0).getAmount());
    }
}