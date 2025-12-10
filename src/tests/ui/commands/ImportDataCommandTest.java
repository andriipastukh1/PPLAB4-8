package tests.ui.commands;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;
import ui.commands.ImportDataCommand;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImportDataCommandTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private TaxService svc;
    private Path tempFile;
    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() throws IOException {
        svc = TaxService.getInstance();
        svc.getAllPersons().clear();
        System.setOut(new PrintStream(outputStream));
        tempFile = Files.createTempFile("test_import", ".ser");
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setIn(originalIn);
        System.setOut(originalOut);
        Files.deleteIfExists(tempFile);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testImportValidData() {
        Person p1 = new Person("Data", "Loader");
        svc.addPerson(p1);
        svc.save(tempFile.toString());
        svc.getAllPersons().clear();

        assertEquals(0, svc.getAllPersons().size());

        String input = tempFile.toString() + NL;
        provideInput(input);

        new ImportDataCommand().execute();

        assertEquals(1, svc.getAllPersons().size());
        assertEquals("Data", svc.getAllPersons().get(0).getFirstName());
        assertTrue(outputStream.toString().contains("Loaded persons: 1"));
    }

    @Test
    void testImportFileNotFound() {
        String input = "non_existent_file.ser" + NL;
        provideInput(input);

        new ImportDataCommand().execute();

        assertTrue(outputStream.toString().contains("File not found"));
        assertEquals(0, svc.getAllPersons().size());
    }

    @Test
    void testImportEmptyPath() {
        String input = "" + NL;
        provideInput(input);

        new ImportDataCommand().execute();

        assertTrue(outputStream.toString().contains("Loaded persons: 0"));
    }

    @Test
    void testImportCorruptedFile() throws IOException {
        Files.writeString(tempFile, "This is not a serialized object");

        String input = tempFile.toString() + NL;
        provideInput(input);

        new ImportDataCommand().execute();

        assertTrue(outputStream.toString().contains("Load error"));
        assertEquals(0, svc.getAllPersons().size());
    }

    @Test
    void testGetDesc() {
        assertEquals("Import Data", new ImportDataCommand().getDesc());
    }
}