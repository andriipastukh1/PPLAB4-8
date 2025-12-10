package tests.ui.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.SetAutoSaveCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SetAutoSaveCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        svc = TaxService.getInstance();
        svc.setAutoSaveBatch(null, false);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        svc.setAutoSaveBatch(null, false);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testEnableAutoSave() {
        String input = "y" + NL + "batch.log" + NL;
        provideInput(input);

        new SetAutoSaveCommand().execute();

        assertTrue(outputStream.toString().contains("Auto-save enabled"));
        assertTrue(svc.isAutoSaveEnabled());
        assertEquals("batch.log", svc.getBatchLogPath());
    }

    @Test
    void testDisableAutoSave() {
        svc.setAutoSaveBatch("old.log", true);

        String input = "n" + NL;
        provideInput(input);

        new SetAutoSaveCommand().execute();

        assertTrue(outputStream.toString().contains("Auto-save disabled"));
        assertFalse(svc.isAutoSaveEnabled());
    }

    @Test
    void testGetDesc() {
        assertEquals("Set Auto-Save", new SetAutoSaveCommand().getDesc());
    }
}