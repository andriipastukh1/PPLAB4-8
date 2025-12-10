package tests.ui.commands;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CategoryService;
import service.TaxService;
import ui.commands.EnterIncomeCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EnterIncomeCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private CategoryService catSvc;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
        catSvc = CategoryService.getInstance();
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
    void testPersonNotFoundAndCancel() {
        String input = "Unknown User" + NL + "n" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
        assertNull(svc.findPersonByFullName("Unknown User"));
    }

    @Test
    void testCreateNewPersonAndAddJobIncome() {
        String input = "New User" + NL +
                "y" + NL +
                "1" + NL +
                "1" + NL +
                "5000" + NL +
                "2025-05-20" + NL +
                "Google Inc" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        Person p = svc.findPersonByFullName("New User");
        assertNotNull(p);
        assertEquals(1, p.incomes.size());

        Income income = p.incomes.get(0);
        assertTrue(income instanceof JobIncome);
        assertEquals(new BigDecimal("5000"), income.getAmount());
        assertEquals(LocalDate.of(2025, 5, 20), income.getDate());
        assertEquals("Google Inc", ((JobIncome) income).getEmployer());
    }

    @Test
    void testAddGiftIncomeMonetary() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "2" + NL +
                "1000" + NL +
                "" + NL +
                "Grandma" + NL +
                "y" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");
        Income income = p.incomes.get(0);
        assertTrue(income instanceof GiftIncome);
        assertEquals("Grandma", ((GiftIncome) income).getGiver());
        assertTrue(((GiftIncome) income).isMonetary());
        assertEquals(LocalDate.now(), income.getDate());
    }

    @Test
    void testAddForeignIncome() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "3" + NL +
                "2000" + NL +
                "2025-01-01" + NL +
                "USA" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");
        Income income = p.incomes.get(0);
        assertTrue(income instanceof ForeignIncome);
        assertEquals("USA", ((ForeignIncome) income).getOriginCountry());
    }

    @Test
    void testAddPropertySaleIncome() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "4" + NL +
                "50000" + NL +
                "2025-06-01" + NL +
                "30000" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");

        assertEquals(1, p.properties.size());
        assertEquals(new BigDecimal("30000"), p.properties.get(0).getPurchasePrice());
        assertEquals(new BigDecimal("50000"), p.properties.get(0).getEstimatedValue());

        Income income = p.incomes.get(0);
        assertTrue(income instanceof PropertySaleIncome);
        assertEquals(new BigDecimal("50000"), income.getAmount());
    }

    @Test
    void testAddRoyaltyIncome() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "5" + NL +
                "10000" + NL +
                "2025-08-01" + NL +
                "My Book" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");
        Income income = p.incomes.get(0);
        assertTrue(income instanceof RoyaltyIncome);
        assertEquals("My Book", ((RoyaltyIncome) income).getIntellectualProperty());
    }

    @Test
    void testAddSubcategoryDuringIncomeEntry() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "a" + NL +
                "NewIncomeCat" + NL +
                "1" + NL +
                "100" + NL +
                "" + NL +
                "Emp" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        assertTrue(outputStream.toString().contains("Added subcategory"));
        assertTrue(catSvc.getSubcategories("INCOME").contains("NewIncomeCat"));
    }

    @Test
    void testInvalidDateFormatDefaultsToNow() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "1" + NL +
                "100" + NL +
                "invalid-date" + NL +
                "Employer" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        assertTrue(outputStream.toString().contains("Invalid date format"));

        Person p = svc.findPersonByFullName("Ivan Test");
        assertEquals(LocalDate.now(), p.incomes.get(0).getDate());
    }

    @Test
    void testUnknownTypeCancelsOperation() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "99" + NL +
                "100" + NL +
                "" + NL;
        provideInput(input);

        new EnterIncomeCommand().execute();

        assertTrue(outputStream.toString().contains("Unknown type selected"));
        Person p = svc.findPersonByFullName("Ivan Test");
        assertTrue(p.incomes.isEmpty());
    }

    @Test
    void testGetDesc() {
        assertEquals("Enter Income", new EnterIncomeCommand().getDesc());
    }
}