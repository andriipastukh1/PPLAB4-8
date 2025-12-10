package tests.ui.commands;

import model.JobIncome;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.IncomeMenuCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IncomeMenuCommandTest {

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
    void testExitMenu() {
        String input = "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Returning to main menu"));
    }

    @Test
    void testInvalidOptionThenExit() {
        String input = "99" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid choice"));
    }

    @Test
    void testNonNumericOptionThenExit() {
        String input = "abc" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Error: Please enter a number"));
    }

    @Test
    void testListIncomesPersonNotFound() {
        String input = "2" + NL + "Unknown Person" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testListIncomesEmpty() {
        svc.createPerson("Ivan", "NoIncome");
        String input = "2" + NL + "Ivan NoIncome" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("No incomes for Ivan NoIncome"));
    }

    @Test
    void testListIncomesSuccess() {
        Person p = svc.createPerson("Ivan", "Rich");
        p.addIncome(new JobIncome(new BigDecimal("1000"), LocalDate.now(), "Job", "Salary"));

        String input = "2" + NL + "Ivan Rich" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Incomes:"));
        assertTrue(output.contains("JobIncome"));
        assertTrue(output.contains("1000"));
        assertTrue(output.contains("Salary"));
    }

    @Test
    void testDeleteIncomePersonNotFound() {
        String input = "3" + NL + "Ghost" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testDeleteIncomeListEmpty() {
        svc.createPerson("Ivan", "Empty");
        String input = "3" + NL + "Ivan Empty" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("No incomes to delete"));
    }

    @Test
    void testDeleteIncomeSuccess() {
        Person p = svc.createPerson("Ivan", "Delete");
        p.addIncome(new JobIncome(BigDecimal.TEN, LocalDate.now(), "J", "N"));

        assertEquals(1, p.incomes.size());

        String input = "3" + NL + "Ivan Delete" + NL + "0" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        assertEquals(0, p.incomes.size());
        assertTrue(outputStream.toString().contains("Income removed"));
    }

    @Test
    void testDeleteIncomeInvalidIndexHigh() {
        Person p = svc.createPerson("Ivan", "Delete");
        p.addIncome(new JobIncome(BigDecimal.TEN, LocalDate.now(), "J", "N"));

        String input = "3" + NL + "Ivan Delete" + NL + "5" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        assertEquals(1, p.incomes.size());
        assertTrue(outputStream.toString().contains("Index out of range"));
    }

    @Test
    void testDeleteIncomeInvalidIndexLow() {
        Person p = svc.createPerson("Ivan", "Delete");
        p.addIncome(new JobIncome(BigDecimal.TEN, LocalDate.now(), "J", "N"));

        String input = "3" + NL + "Ivan Delete" + NL + "-1" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        assertEquals(1, p.incomes.size());
        assertTrue(outputStream.toString().contains("Index out of range"));
    }

    @Test
    void testDeleteIncomeInvalidFormat() {
        Person p = svc.createPerson("Ivan", "Delete");
        p.addIncome(new JobIncome(BigDecimal.TEN, LocalDate.now(), "J", "N"));

        String input = "3" + NL + "Ivan Delete" + NL + "text" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        assertEquals(1, p.incomes.size());
        assertTrue(outputStream.toString().contains("Invalid number"));
    }

    @Test
    void testEnterIncomeCommandIntegration() {
        String input = "1" + NL + "New User" + NL + "n" + NL + "0" + NL;
        provideInput(input);

        new IncomeMenuCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Enter person full name"));
    }

    @Test
    void testGetDesc() {
        assertTrue(new IncomeMenuCommand().getDesc().contains("Income Management"));
    }
}