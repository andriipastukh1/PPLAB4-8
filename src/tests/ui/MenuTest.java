package tests.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Menu;
import ui.commands.Command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private Menu menu;

    static class MockCommand implements Command {
        boolean wasExecuted = false;

        @Override
        public void execute() {
            wasExecuted = true;
        }

        @Override
        public String getDesc() {
            return "Mock Command";
        }
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        menu = new Menu();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    private void injectMockCommand(int key, Command mockCommand) {
        try {
            Field field = Menu.class.getDeclaredField("commands");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Integer, Command> commands = (Map<Integer, Command>) field.get(menu);
            commands.put(key, mockCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldExitImmediately() {
        provideInput("0\n");
        menu.execute();
        String output = outputStream.toString();
        assertTrue(output.contains("Exiting system"));
    }

    @Test
    void shouldExecuteValidCommand() {
        MockCommand myMock = new MockCommand();
        injectMockCommand(99, myMock);

        provideInput("99\n0\n");
        menu.execute();

        assertTrue(myMock.wasExecuted, "Command should be executed");
        assertTrue(outputStream.toString().contains("99. Mock Command"));
    }

    @Test
    void shouldHandleInvalidIntegerChoice() {
        provideInput("999\n0\n");

        menu.execute();
        assertTrue(outputStream.toString().contains("Error: Invalid choice"));
    }

    @Test
    void shouldHandleNonIntegerInput() {
        provideInput("abc\n0\n");
        menu.execute();
        assertTrue(outputStream.toString().contains("Error: Please enter a number"));
    }

    @Test
    void shouldShowMainMenu() {
        provideInput("0\n");
        menu.execute();
        assertTrue(outputStream.toString().contains("TAX SYSTEM MAIN MENU"));
        assertTrue(outputStream.toString().contains("0. Exit"));
        assertTrue(outputStream.toString().contains("1. Exit"));

    }

    @Test
    void shouldReturnCorrectDescription() {
        assertEquals("Main Menu", menu.getDesc());
    }
}