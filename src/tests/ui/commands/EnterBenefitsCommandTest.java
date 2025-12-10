package tests.ui.commands;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CategoryService;
import service.TaxService;
import ui.commands.EnterBenefitsCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EnterBenefitsCommandTest {

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
    void testPersonNotFound() {
        String input = "Unknown User" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Person not found"));
    }

    @Test
    void testAddFixedAmountBenefitSuccess() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "MyBenefit" + NL +
                "500" + NL +
                "n" + NL +
                "2020" + NL +
                "2025" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");
        assertEquals(1, p.benefits.size());
        assertEquals("MyBenefit", p.benefits.get(0).getDescription());
        assertEquals(new BigDecimal("500"), p.benefits.get(0).getAmount());
        assertFalse(p.benefits.get(0).isPercent());
        assertEquals(2020, p.benefits.get(0).getValidFromYear());
        assertEquals(2025, p.benefits.get(0).getValidToYear());
    }

    @Test
    void testAddPercentageBenefitSuccess() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "TaxCut" + NL +
                "10" + NL +
                "y" + NL +
                "2023" + NL +
                "2024" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        Person p = svc.findPersonByFullName("Ivan Test");
        assertEquals(1, p.benefits.size());
        assertEquals("TaxCut", p.benefits.get(0).getDescription());
        assertEquals(new BigDecimal("10"), p.benefits.get(0).getAmount());
        assertTrue(p.benefits.get(0).isPercent());
    }

    @Test
    void testAddNewSubcategory() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "a" + NL +
                "NewCategory" + NL +
                "NewCategoryBenefit" + NL +
                "100" + NL +
                "n" + NL +
                "2025" + NL +
                "2025" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        assertTrue(outputStream.toString().contains("Added subcategory"));
        assertTrue(catSvc.getSubcategories("BENEFIT").contains("NewCategory"));

        Person p = svc.findPersonByFullName("Ivan Test");
        assertEquals(1, p.benefits.size());
    }

    @Test
    void testAddExistingSubcategoryFailure() {
        svc.createPerson("Ivan", "Test");
        catSvc.addSubcategory("BENEFIT", "ExistingCat");

        String input = "Ivan Test" + NL +
                "a" + NL +
                "ExistingCat" + NL +
                "Benefit" + NL +
                "100" + NL +
                "n" + NL +
                "2020" + NL +
                "2021" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        assertTrue(outputStream.toString().contains("Not added (exists)"));
    }

    @Test
    void testInvalidAmountInput() {
        svc.createPerson("Ivan", "Test");

        String input = "Ivan Test" + NL +
                "1" + NL +
                "Benefit" + NL +
                "abc" + NL +
                "100" + NL +
                "n" + NL +
                "2020" + NL +
                "2021" + NL;
        provideInput(input);

        new EnterBenefitsCommand().execute();

        assertTrue(outputStream.toString().contains("Error: Invalid amount"));

        Person p = svc.findPersonByFullName("Ivan Test");
        assertEquals(new BigDecimal("100"), p.benefits.get(0).getAmount());
    }

    @Test
    void testGetDesc() {
        assertEquals("Enter Benefit", new EnterBenefitsCommand().getDesc());
    }
}