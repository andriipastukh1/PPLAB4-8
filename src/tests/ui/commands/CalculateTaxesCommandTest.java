package tests.ui.commands;

import model.JobIncome;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.CalculateTaxesCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalculateTaxesCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testExecutePersonNotFound() {
        String input = "Unknown User" + NL;
        provideInput(input);

        new CalculateTaxesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testExecuteSuccess() {
        Person p = svc.createPerson("Tax", "Payer");
        int year = LocalDate.now().getYear();
        p.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Job", "n"));

        String input = "Tax Payer" + NL + year + NL;
        provideInput(input);

        new CalculateTaxesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Taxes for Tax Payer"));
        assertTrue(output.contains("INCOME_TAX"));
        assertTrue(output.contains("195.0"));
    }

    @Test
    void testExecuteWithZeroIncome() {
        Person p = svc.createPerson("Zero", "Income");
        int year = LocalDate.now().getYear();

        String input = "Zero Income" + NL + year + NL;
        provideInput(input);

        new CalculateTaxesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Total tax: 0"));
        assertFalse(output.contains("Tax burden"));
    }

    @Test
    void testExecuteShowsTaxBurden() {
        Person p = svc.createPerson("Burden", "Test");
        int year = LocalDate.now().getYear();
        p.addIncome(new JobIncome(new BigDecimal("10000.00"), LocalDate.now(), "Job", "n"));

        String input = "Burden Test" + NL + year + NL;
        provideInput(input);

        new CalculateTaxesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Tax burden:"));
        assertTrue(output.contains("%"));
    }
}