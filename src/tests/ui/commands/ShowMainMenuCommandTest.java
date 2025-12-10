package tests.ui.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.commands.ShowMainMenuCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ShowMainMenuCommandTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testExecutePrintsHeader() {
        new ShowMainMenuCommand().execute();

        String output = outputStream.toString();

        assertTrue(output.toUpperCase().contains("MENU"));
        assertTrue(output.contains("---"));
    }

    @Test
    void testExecutePrintsCoreOptions() {
        new ShowMainMenuCommand().execute();

        String output = outputStream.toString();

        assertTrue(output.contains("1."));
        assertTrue(output.contains("2."));
        assertTrue(output.contains("0. Exit"));
        assertTrue(output.contains("Income"));
        assertTrue(output.contains("Benefit"));
        assertTrue(output.contains("Calculate"));
    }

    @Test
    void testExecuteOutputIsNotEmpty() {
        new ShowMainMenuCommand().execute();
        assertFalse(outputStream.toString().isBlank());
    }

    @Test
    void testGetDesc() {
        String desc = new ShowMainMenuCommand().getDesc();
        assertNotNull(desc);
        assertTrue(desc.contains("Menu"));
    }
}