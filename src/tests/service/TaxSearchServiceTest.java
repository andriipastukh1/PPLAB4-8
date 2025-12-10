package tests.service;

import model.TaxCategory;
import model.TaxPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TaxSearchService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxSearchServiceTest {

    private TaxSearchService searchService;
    private List<TaxPayment> testData;

    @BeforeEach
    void setUp() {
        searchService = new TaxSearchService();
        testData = new ArrayList<>();
        testData.add(new TaxPayment(new BigDecimal("100.00"), 2025, "A", TaxCategory.INCOME_TAX));
        testData.add(new TaxPayment(new BigDecimal("50.00"), 2025, "B", TaxCategory.GIFT_TAX));
        testData.add(new TaxPayment(new BigDecimal("200.00"), 2025, "C", TaxCategory.PROPERTY_TAX));
        testData.add(new TaxPayment(new BigDecimal("10.00"), 2025, "D", TaxCategory.MILITARY_TAX));
        testData.add(new TaxPayment(new BigDecimal("500.00"), 2025, "E", TaxCategory.FOREIGN_INCOME_TAX));
    }

    @Test
    void testFindTaxesInSpecificRange() {
        BigDecimal min = new BigDecimal("40.00");
        BigDecimal max = new BigDecimal("150.00");

        List<TaxPayment> result = searchService.findTaxesByRange(testData, min, max);

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("50.00"), result.get(0).getAmount());
        assertEquals(new BigDecimal("100.00"), result.get(1).getAmount());
    }

    @Test
    void testFindTaxesMinBoundOnly() {
        BigDecimal min = new BigDecimal("150.00");

        List<TaxPayment> result = searchService.findTaxesByRange(testData, min, null);

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("200.00"), result.get(0).getAmount());
        assertEquals(new BigDecimal("500.00"), result.get(1).getAmount());
    }

    @Test
    void testFindTaxesMaxBoundOnly() {
        BigDecimal max = new BigDecimal("60.00");

        List<TaxPayment> result = searchService.findTaxesByRange(testData, null, max);

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("10.00"), result.get(0).getAmount());
        assertEquals(new BigDecimal("50.00"), result.get(1).getAmount());
    }

    @Test
    void testFindTaxesNoBounds() {
        List<TaxPayment> result = searchService.findTaxesByRange(testData, null, null);

        assertEquals(5, result.size());
        assertEquals(new BigDecimal("10.00"), result.get(0).getAmount());
        assertEquals(new BigDecimal("500.00"), result.get(4).getAmount());
    }

    @Test
    void testFindTaxesExactMatch() {
        BigDecimal exact = new BigDecimal("100.00");

        List<TaxPayment> result = searchService.findTaxesByRange(testData, exact, exact);

        assertEquals(1, result.size());
        assertEquals(exact, result.get(0).getAmount());
    }

    @Test
    void testFindTaxesEmptyInput() {
        List<TaxPayment> empty = new ArrayList<>();
        List<TaxPayment> result = searchService.findTaxesByRange(empty, BigDecimal.ZERO, BigDecimal.TEN);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindTaxesNoneFound() {
        BigDecimal min = new BigDecimal("1000.00");
        BigDecimal max = new BigDecimal("2000.00");

        List<TaxPayment> result = searchService.findTaxesByRange(testData, min, max);

        assertTrue(result.isEmpty());
    }
}