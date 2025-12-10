package tests.ui.commands;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.ShowAllDataCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ShowAllDataCommandTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecuteWithEmptyDatabase() {
        new ShowAllDataCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("No persons found"));
    }

    @Test
    void testExecuteWithSinglePersonNoDetails() {
        svc.createPerson("Simple", "User");

        new ShowAllDataCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("--- All Data ---"));
        assertTrue(output.contains("Simple User"));
        assertTrue(output.contains("Incomes: 0"));
        assertTrue(output.contains("Benefits: 0"));
        assertTrue(output.contains("Properties: 0"));
    }

    @Test
    void testExecuteWithFullPersonDetails() {
        Person p = svc.createPerson("Complex", "User");

        p.addIncome(new JobIncome(new BigDecimal("5000.00"), LocalDate.of(2025, 1, 1), "Google", "Salary"));
        p.addIncome(new GiftIncome(new BigDecimal("1000.00"), LocalDate.of(2025, 2, 1), "Mom", true, "Gift"));

        p.addBenefit(new TaxBenefit("Child Support", new BigDecimal("500.00"), false, 2020, 2030));

        Property prop = new Property();
        prop.setDescription("Downtown Apartment");
        prop.setEstimatedValue(new BigDecimal("100000.00"));
        p.addProperty(prop);

        p.addTaxPayment(new TaxPayment(new BigDecimal("123.00"), 2025, "Tax Payment", TaxCategory.INCOME_TAX));

        new ShowAllDataCommand().execute();

        String output = outputStream.toString();

        assertTrue(output.contains("Complex User"));

        assertTrue(output.contains("JobIncome"));
        assertTrue(output.contains("5000.00"));
        assertTrue(output.contains("Google"));

        assertTrue(output.contains("GiftIncome"));
        assertTrue(output.contains("1000.00"));
        assertTrue(output.contains("Mom"));

        assertTrue(output.contains("Child Support"));
        assertTrue(output.contains("500.00"));

        assertTrue(output.contains("Downtown Apartment"));
        assertTrue(output.contains("100000.00"));

        assertTrue(output.contains("TaxPayment"));
        assertTrue(output.contains("123.00"));
        assertTrue(output.contains("INCOME_TAX"));
    }

    @Test
    void testExecuteWithMultiplePersons() {
        svc.createPerson("Person", "One");
        svc.createPerson("Person", "Two");
        svc.createPerson("Person", "Three");

        new ShowAllDataCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person One"));
        assertTrue(output.contains("Person Two"));
        assertTrue(output.contains("Person Three"));
        assertTrue(output.contains("Total persons: 3"));
    }

    @Test
    void testGetDesc() {
        String desc = new ShowAllDataCommand().getDesc();
        assertNotNull(desc);
        assertEquals("Show All Data", desc);
    }
}