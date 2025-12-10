package tests.ui.commands;

import model.JobIncome;
import model.Person;
import model.RoyaltyIncome;
import model.TaxBenefit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.ShowReportCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ShowReportCommandTest {

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
        String input = "NonExistent User" + NL + "2025" + NL;
        provideInput(input);

        new ShowReportCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testReportGenerationSuccess() {
        Person p = svc.createPerson("John", "Doe");
        p.addIncome(new JobIncome(new BigDecimal("10000.00"), LocalDate.now(), "Google", "Salary"));
        int year = LocalDate.now().getYear();

        String input = "John Doe" + NL + year + NL;
        provideInput(input);

        new ShowReportCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("TAX REPORT"));
        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains(String.valueOf(year)));
        assertTrue(output.contains("10000.00"));
        assertTrue(output.contains("1950.0"));
    }

    @Test
    void testReportWithMultipleIncomes() {
        Person p = svc.createPerson("Multi", "Source");
        int year = LocalDate.now().getYear();

        p.addIncome(new JobIncome(new BigDecimal("5000.00"), LocalDate.now(), "Job", "Main"));
        p.addIncome(new RoyaltyIncome(new BigDecimal("2000.00"), LocalDate.now(), "Book", "Side"));

        String input = "Multi Source" + NL + year + NL;
        provideInput(input);

        new ShowReportCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("JobIncome"));
        assertTrue(output.contains("RoyaltyIncome"));
        assertTrue(output.contains("7000.0"));
    }

    @Test
    void testReportWithBenefits() {
        Person p = svc.createPerson("Beneficiary", "User");
        int year = LocalDate.now().getYear();

        p.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Job", "n"));
        p.addBenefit(new TaxBenefit("Charity", new BigDecimal("100.00"), false, year, year));

        String input = "Beneficiary User" + NL + year + NL;
        provideInput(input);

        new ShowReportCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Benefits applied"));
        assertTrue(output.contains("Charity"));
    }

    @Test
    void testReportForEmptyYear() {
        Person p = svc.createPerson("Old", "Data");
        int currentYear = LocalDate.now().getYear();
        int oldYear = currentYear - 5;

        p.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.of(oldYear, 1, 1), "OldJob", "n"));

        String input = "Old Data" + NL + currentYear + NL;
        provideInput(input);

        new ShowReportCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Total income: 0"));
        assertTrue(output.contains("Total tax: 0"));
    }

    @Test
    void testGetDesc() {
        String desc = new ShowReportCommand().getDesc();
        assertNotNull(desc);
        assertTrue(desc.contains("Report"));
    }
}