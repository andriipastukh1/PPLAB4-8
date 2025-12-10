package tests.service;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxCalculator;
import service.TaxReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxReportGeneratorTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxReportGenerator generator;
    private TaxCalculator calculator;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        generator = new TaxReportGenerator();
        calculator = new TaxCalculator();
        calculator.updateRate(TaxCategory.INCOME_TAX, new BigDecimal("0.18"));
        calculator.updateRate(TaxCategory.MILITARY_TAX, new BigDecimal("0.015"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testGenerateReport() {
        Person person = new Person("Report", "User");
        int year = LocalDate.now().getYear();

        person.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Job", "Salary"));

        String report = generator.generateReport(List.of(person), calculator, year);

        assertNotNull(report);
        assertTrue(report.contains("TAX REPORT"));
        assertTrue(report.contains("Report User"));
        assertTrue(report.contains("1000.00"));
        assertTrue(report.contains("INCOME_TAX"));
        assertTrue(report.contains("195.0"));
    }

    @Test
    void testGenerateReportEmpty() {
        String report = generator.generateReport(List.of(), calculator, 2025);

        assertNotNull(report);
        assertTrue(report.contains("TAX REPORT"));
        assertFalse(report.contains("Total income"));
    }

    @Test
    void testGenerateReceipt() {
        TaxPayment payment = new TaxPayment(new BigDecimal("123.45"), 2025, "Test Payment", TaxCategory.GIFT_TAX);

        generator.generateReceipt(payment);

        String output = outputStream.toString();

        assertTrue(output.contains("RECEIPT"));
        assertTrue(output.contains("123.45"));
        assertTrue(output.contains("Test Payment"));
        assertTrue(output.contains("GIFT_TAX"));
    }
}