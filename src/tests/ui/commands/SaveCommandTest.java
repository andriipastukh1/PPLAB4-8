package tests.ui.commands;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.SaveCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SaveCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private final String NL = System.lineSeparator();
    private final Path defaultPath = Paths.get("data.ser");
    private Path customPath;

    @BeforeEach
    void setUp() throws IOException {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
        System.setOut(new PrintStream(outputStream));
        customPath = Files.createTempFile("custom_save", ".ser");
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setIn(originalIn);
        System.setOut(originalOut);
        Files.deleteIfExists(defaultPath);
        Files.deleteIfExists(customPath);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testSaveDefault() {
        svc.addPerson(new Person("Default", "Save"));

        String input = "" + NL;
        provideInput(input);

        new SaveCommand().execute();

        assertTrue(Files.exists(defaultPath));
        assertTrue(outputStream.toString().contains("Saved."));
    }

    @Test
    void testSaveCustomPath() {
        svc.addPerson(new Person("Custom", "Save"));

        String input = customPath.toString() + NL;
        provideInput(input);

        new SaveCommand().execute();

        assertTrue(Files.exists(customPath));
        assertTrue(outputStream.toString().contains("Saved."));
    }

    @Test
    void testSaveWithEmptyData() {
        svc.getAllPersons().clear();

        String input = customPath.toString() + NL;
        provideInput(input);

        new SaveCommand().execute();

        assertTrue(Files.exists(customPath));
        assertTrue(outputStream.toString().contains("Saved 0 persons"));
    }

    @Test
    void testGetDesc() {
        assertEquals("Save Command", new SaveCommand().getDesc());
    }
}