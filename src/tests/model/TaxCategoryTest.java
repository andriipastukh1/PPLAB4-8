package tests.model;

import model.TaxCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxCategoryTest {

    @Test
    void testEnumConstantsExistence() {
        assertNotNull(TaxCategory.INCOME_TAX);
        assertNotNull(TaxCategory.MILITARY_TAX);
        assertNotNull(TaxCategory.PROPERTY_TAX);
        assertNotNull(TaxCategory.GIFT_TAX);
        assertNotNull(TaxCategory.FOREIGN_INCOME_TAX);
        assertNotNull(TaxCategory.CAPITAL_GAINS);
        assertNotNull(TaxCategory.ROYALTY_TAX);
    }

    @Test
    void testDefaultRates() {
        assertEquals(0.18, TaxCategory.INCOME_TAX.getDefaultRate());
        assertEquals(0.015, TaxCategory.MILITARY_TAX.getDefaultRate());
        assertEquals(0.05, TaxCategory.PROPERTY_TAX.getDefaultRate());
        assertEquals(0.10, TaxCategory.GIFT_TAX.getDefaultRate());
        assertEquals(0.20, TaxCategory.FOREIGN_INCOME_TAX.getDefaultRate());
        assertEquals(0.10, TaxCategory.CAPITAL_GAINS.getDefaultRate());
        assertEquals(0.10, TaxCategory.ROYALTY_TAX.getDefaultRate());
    }

    @Test
    void testDescriptions() {
        assertEquals("Income tax (PIT)", TaxCategory.INCOME_TAX.getDescription());
        assertEquals("Military tax", TaxCategory.MILITARY_TAX.getDescription());
        assertNotNull(TaxCategory.PROPERTY_TAX.getDescription());
        assertFalse(TaxCategory.GIFT_TAX.getDescription().isBlank());
    }

    @Test
    void testValueOf() {
        TaxCategory category = TaxCategory.valueOf("INCOME_TAX");
        assertEquals(TaxCategory.INCOME_TAX, category);
    }

    @Test
    void testValues() {
        TaxCategory[] values = TaxCategory.values();
        assertTrue(values.length >= 7);

        boolean foundIncome = false;
        for (TaxCategory c : values) {
            if (c == TaxCategory.INCOME_TAX) {
                foundIncome = true;
                break;
            }
        }
        assertTrue(foundIncome);
    }
}