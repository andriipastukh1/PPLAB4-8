package tests.ui.commands;

import model.JobIncome;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.RegisterPaymentCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterPaymentCommandTest {

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
    void testPersonNotFound() {
        String input = "Unknown User" + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testNoTaxesFound() {
        svc.createPerson("Ivan", "NoTax");
        int year = LocalDate.now().getYear();

        String input = "Ivan NoTax" + NL + year + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("No taxes found"));
    }

    @Test
    void testPaySingleTaxSuccess() {
        Person p = svc.createPerson("Ivan", "Payer");
        p.addIncome(new JobIncome(new BigDecimal("1000"), LocalDate.now(), "Job", "n"));
        int year = LocalDate.now().getYear();

        String input = "Ivan Payer" + NL + year + NL + "0" + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Marked paid"));
        assertEquals(1, p.taxPayments.size());
        assertTrue(p.taxPayments.get(0).isPaid());
    }

    @Test
    void testPayAllTaxesSuccess() {
        Person p = svc.createPerson("Ivan", "Rich");
        p.addIncome(new JobIncome(new BigDecimal("1000"), LocalDate.now(), "Job1", "n"));
        p.addIncome(new JobIncome(new BigDecimal("2000"), LocalDate.now(), "Job2", "n"));
        int year = LocalDate.now().getYear();

        String input = "Ivan Rich" + NL + year + NL + "all" + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Marked all as paid"));
        assertEquals(2, p.taxPayments.size());
        assertTrue(p.taxPayments.get(0).isPaid());
        assertTrue(p.taxPayments.get(1).isPaid());
    }

    @Test
    void testIndexOutOfRange() {
        Person p = svc.createPerson("Ivan", "Fail");
        p.addIncome(new JobIncome(new BigDecimal("1000"), LocalDate.now(), "Job", "n"));
        int year = LocalDate.now().getYear();

        String input = "Ivan Fail" + NL + year + NL + "5" + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Index out of range"));
        assertEquals(0, p.taxPayments.size());
    }

    @Test
    void testInvalidInputFormat() {
        Person p = svc.createPerson("Ivan", "Text");
        p.addIncome(new JobIncome(new BigDecimal("1000"), LocalDate.now(), "Job", "n"));
        int year = LocalDate.now().getYear();

        String input = "Ivan Text" + NL + year + NL + "abc" + NL;
        provideInput(input);

        new RegisterPaymentCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input"));
        assertEquals(0, p.taxPayments.size());
    }

    @Test
    void testGetDesc() {
        assertEquals("Register Payment", new RegisterPaymentCommand().getDesc());
    }
}