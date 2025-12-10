package tests.service;


import model.JobIncome;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.StorageManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageManagerTest {

    private StorageManager storageManager;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        storageManager = new StorageManager();
        tempFile = Files.createTempFile("test_storage", ".ser");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSaveAndLoadValidData() {
        List<Person> persons = new ArrayList<>();
        Person p1 = new Person("John", "Doe");
        p1.addIncome(new JobIncome(new BigDecimal("1000.00"), LocalDate.now(), "Google", "Salary"));

        Person p2 = new Person("Jane", "Smith");

        persons.add(p1);
        persons.add(p2);

        storageManager.saveToFile(persons, tempFile.toString());

        List<Person> loadedPersons = storageManager.loadFromFile(tempFile.toString());

        assertNotNull(loadedPersons);
        assertEquals(2, loadedPersons.size());

        Person loadedP1 = loadedPersons.get(0);
        assertEquals("John", loadedP1.getFirstName());
        assertEquals(1, loadedP1.incomes.size());
        assertEquals(new BigDecimal("1000.00"), loadedP1.incomes.get(0).getAmount());

        assertEquals("Jane", loadedPersons.get(1).getFirstName());
    }

    @Test
    void testSaveAndLoadEmptyList() {
        List<Person> emptyList = new ArrayList<>();

        storageManager.saveToFile(emptyList, tempFile.toString());

        List<Person> loaded = storageManager.loadFromFile(tempFile.toString());

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    void testLoadFromFileNotFound() {
        String nonExistentPath = "invalid_path/file.ser";

        List<Person> result = storageManager.loadFromFile(nonExistentPath);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testLoadFromCorruptedFile() throws IOException {
        Files.writeString(tempFile, "This is not a serialized object");

        List<Person> result = storageManager.loadFromFile(tempFile.toString());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveToInvalidPath() {
        // Attempt to save to a directory that doesn't exist/permission denied
        // Should catch IOException and print, but not crash
        String invalidPath = "/root/restricted/test.ser";

        // Code handles exception internally
        storageManager.saveToFile(new ArrayList<>(), invalidPath);

        // If we reached here without exception, test passes (behavior verification)
        assertTrue(true);
    }
}