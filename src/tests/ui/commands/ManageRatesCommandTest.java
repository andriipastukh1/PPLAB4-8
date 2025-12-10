package tests.ui.commands;

import model.TaxCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.ManageRatesCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ManageRatesCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();

        // Reset rates to default for consistent testing
        svc.getCalculator().updateRate(TaxCategory.INCOME_TAX, new BigDecimal("0.18"));

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
    void testShowRatesAndExit() {
        String input = "n" + NL;
        provideInput(input);

        new ManageRatesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Current Tax Rates"));
        assertTrue(output.contains("INCOME_TAX = 0.18"));
        assertFalse(output.contains("Updated"));
    }

    @Test
    void testUpdateRateSuccess() {
        String input = "y" + NL +
                "INCOME_TAX" + NL +
                "0.25" + NL;
        provideInput(input);

        new ManageRatesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Updated INCOME_TAX -> 0.25"));

        BigDecimal newRate = svc.getCalculator().getRates().get(TaxCategory.INCOME_TAX);
        assertEquals(new BigDecimal("0.25"), newRate);
    }

    @Test
    void testUpdateRateLowerCaseCategoryInput() {
        String input = "y" + NL +
                "income_tax" + NL +
                "0.30" + NL;
        provideInput(input);

        new ManageRatesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Updated INCOME_TAX -> 0.30"));

        BigDecimal newRate = svc.getCalculator().getRates().get(TaxCategory.INCOME_TAX);
        assertEquals(new BigDecimal("0.30"), newRate);
    }

    @Test
    void testUpdateRateInvalidCategory() {
        String input = "y" + NL +
                "INVALID_CATEGORY" + NL;
        provideInput(input);

        new ManageRatesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Error: Invalid category name"));

        // Rate should not change
        BigDecimal rate = svc.getCalculator().getRates().get(TaxCategory.INCOME_TAX);
        assertEquals(new BigDecimal("0.18"), rate);
    }

    @Test
    void testUpdateRateInvalidNumberFormat() {
        // First enters "abc" (invalid), then "0.22" (valid)
        String input = "y" + NL +
                "INCOME_TAX" + NL +
                "abc" + NL +
                "0.22" + NL;
        provideInput(input);

        new ManageRatesCommand().execute();

        String output = outputStream.toString();
        assertTrue(output.contains("Error: Invalid amount"));
        assertTrue(output.contains("Updated INCOME_TAX -> 0.22"));

        BigDecimal newRate = svc.getCalculator().getRates().get(TaxCategory.INCOME_TAX);
        assertEquals(new BigDecimal("0.22"), newRate);
    }

    @Test
    void testGetDesc() {
        assertEquals("Manage Tax Rates", new ManageRatesCommand().getDesc());
    }
}