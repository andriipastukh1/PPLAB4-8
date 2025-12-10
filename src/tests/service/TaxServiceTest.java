package tests.service;


import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxServiceTest {

    private TaxService service;
    private Path tempFile;
    private Path tempBatchFile;

    @BeforeEach
    void setUp() throws IOException {
        service = TaxService.getInstance();
        service.getAllPersons().clear();
        service.setAutoSaveBatch(null, false);
        tempFile = Files.createTempFile("test_data", ".ser");
        tempBatchFile = Files.createTempFile("test_batch", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempBatchFile);
        service.setAutoSaveBatch(null, false);
    }

    @Test
    void testSingletonInstance() {
        TaxService s1 = TaxService.getInstance();
        TaxService s2 = TaxService.getInstance();
        assertSame(s1, s2);
    }

    @Test
    void testCreatePerson() {
        Person p = service.createPerson("John", "Doe");

        assertNotNull(p);
        assertEquals("John", p.getFirstName());
        assertEquals(1, service.getAllPersons().size());
        assertTrue(service.getAllPersons().contains(p));
    }

    @Test
    void testAddPerson() {
        Person p = new Person("Jane", "Smith");
        service.addPerson(p);

        assertEquals(1, service.getAllPersons().size());
        assertEquals("Jane", service.getAllPersons().get(0).getFirstName());
    }

    @Test
    void testAddNullPerson() {
        service.addPerson(null);
        assertTrue(service.getAllPersons().isEmpty());
    }

    @Test
    void testFindPersonByFullName() {
        service.createPerson("Alice", "Wonderland");
        service.createPerson("Bob", "Builder");

        Person found = service.findPersonByFullName("Bob Builder");
        assertNotNull(found);
        assertEquals("Bob", found.getFirstName());
    }

    @Test
    void testFindPersonByFullNameCaseInsensitive() {
        service.createPerson("Charlie", "Chaplin");

        Person found = service.findPersonByFullName("charlie chaplin");
        assertNotNull(found);
        assertEquals("Charlie", found.getFirstName());
    }

    @Test
    void testFindPersonNotFound() {
        Person found = service.findPersonByFullName("Ghost");
        assertNull(found);
    }

    @Test
    void testFindPersonNullInput() {
        assertNull(service.findPersonByFullName(null));
    }

    @Test
    void testAddIncomeToPerson() {
        Person p = service.createPerson("Rich", "Man");
        Income income = new JobIncome(BigDecimal.TEN, LocalDate.now(), "Work", "Note");

        service.addIncomeToPerson(p, income);

        assertEquals(1, p.incomes.size());
        assertEquals(income, p.incomes.get(0));
    }

    @Test
    void testAddBenefitToPerson() {
        Person p = service.createPerson("Poor", "Man");
        TaxBenefit benefit = new TaxBenefit("Help", BigDecimal.ONE, false, 2020, 2030);

        service.addBenefitToPerson(p, benefit);

        assertEquals(1, p.benefits.size());
        assertEquals(benefit, p.benefits.get(0));
    }

    @Test
    void testSaveAndLoad() {
        Person p1 = new Person("Save", "Me");
        p1.addIncome(new JobIncome(BigDecimal.TEN, LocalDate.now(), "Job", "Desc"));
        service.addPerson(p1);

        service.save(tempFile.toString());

        service.getAllPersons().clear();
        assertTrue(service.getAllPersons().isEmpty());

        service.load(tempFile.toString());

        assertEquals(1, service.getAllPersons().size());
        Person loaded = service.getAllPersons().get(0);
        assertEquals("Save", loaded.getFirstName());
        assertEquals(1, loaded.incomes.size());
    }

    @Test
    void testAutoSaveBatchLogging() throws IOException {
        service.setAutoSaveBatch(tempBatchFile.toString(), true);

        Person p = service.createPerson("Auto", "Save");
        Income income = new JobIncome(new BigDecimal("100"), LocalDate.now(), "TestComp", "Job");

        service.addIncomeToPerson(p, income);

        List<String> lines = Files.readAllLines(tempBatchFile);

        boolean personFound = lines.stream().anyMatch(l -> l.contains("ADD_PERSON|Auto|Save"));
        boolean incomeFound = lines.stream().anyMatch(l -> l.contains("ADD_INCOME|Auto Save|JOB|100"));

        assertTrue(personFound);
        assertTrue(incomeFound);
    }

    @Test
    void testAutoSaveBenefitLogging() throws IOException {
        service.setAutoSaveBatch(tempBatchFile.toString(), true);

        Person p = service.createPerson("Ben", "Efit");
        TaxBenefit tb = new TaxBenefit("Child", BigDecimal.TEN, false, 2020, 2025);

        service.addBenefitToPerson(p, tb);

        List<String> lines = Files.readAllLines(tempBatchFile);
        boolean benefitFound = lines.stream().anyMatch(l -> l.contains("ADD_BENEFIT|Ben Efit|Child"));

        assertTrue(benefitFound);
    }

    @Test
    void testGetCalculator() {
        assertNotNull(service.getCalculator());
    }
}