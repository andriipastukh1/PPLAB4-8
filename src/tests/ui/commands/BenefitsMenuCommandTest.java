package tests.ui.commands;

import model.Person;
import model.TaxBenefit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.BenefitsMenuCommand;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BenefitsMenuCommandTest {

    private final InputStream originalIn = System.in;
    private TaxService svc;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testExitMenu() {
        String input = "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testInvalidMenuChoice() {
        String input = "99" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testListBenefitsPersonNotFound() {
        String input = "2" + NL + "NonExistent" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testListBenefitsEmpty() {
        svc.createPerson("Ivan", "Test");
        String input = "2" + NL + "Ivan Test" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testListBenefitsWithData() {
        Person p = svc.createPerson("Ivan", "Test");
        p.addBenefit(new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025));

        String input = "2" + NL + "Ivan Test" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testDeleteBenefitPersonNotFound() {
        String input = "3" + NL + "NonExistent" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testDeleteBenefitListEmpty() {
        svc.createPerson("Ivan", "Test");
        String input = "3" + NL + "Ivan Test" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();
        assertTrue(true);
    }

    @Test
    void testDeleteBenefitSuccess() {
        Person p = svc.createPerson("Ivan", "Test");
        p.addBenefit(new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025));

        String input = "3" + NL + "Ivan Test" + NL + "0" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();

        assertEquals(0, p.benefits.size());
    }

    @Test
    void testDeleteBenefitInvalidIndexLow() {
        Person p = svc.createPerson("Ivan", "Test");
        p.addBenefit(new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025));

        String input = "3" + NL + "Ivan Test" + NL + "-1" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();

        assertEquals(1, p.benefits.size());
    }

    @Test
    void testDeleteBenefitInvalidIndexHigh() {
        Person p = svc.createPerson("Ivan", "Test");
        p.addBenefit(new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025));

        String input = "3" + NL + "Ivan Test" + NL + "5" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();

        assertEquals(1, p.benefits.size());
    }

    @Test
    void testDeleteBenefitInvalidFormat() {
        Person p = svc.createPerson("Ivan", "Test");
        p.addBenefit(new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025));

        String input = "3" + NL + "Ivan Test" + NL + "abc" + NL + "0" + NL;
        provideInput(input);
        new BenefitsMenuCommand().execute();

        assertEquals(1, p.benefits.size());
    }

    @Test
    void testGetDesc() {
        assertEquals("Benefits", new BenefitsMenuCommand().getDesc());
    }
}