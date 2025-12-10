package tests.model;

import model.Property;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    @Test
    void testIdentityGeneration() {
        Property property = new Property();
        assertNotNull(property.getId());
        assertFalse(property.getId().isBlank());
    }

    @Test
    void testSettersAndGetters() {
        Property property = new Property();
        String desc = "Luxury Apartment";
        BigDecimal price = new BigDecimal("500000.00");
        LocalDate date = LocalDate.of(2020, 1, 1);
        BigDecimal estimated = new BigDecimal("600000.00");

        property.setDescription(desc);
        property.setPurchasePrice(price);
        property.setPurchaseDate(date);
        property.setEstimatedValue(estimated);

        assertEquals(desc, property.getDescription());
        assertEquals(price, property.getPurchasePrice());

        assertEquals(estimated, property.getEstimatedValue());
    }

    @Test
    void testCapitalGainCalculationPositive() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("100000.00"));

        BigDecimal salePrice = new BigDecimal("150000.00");
        BigDecimal gain = property.getCapitalGain(salePrice);

        assertEquals(new BigDecimal("50000.00"), gain);
    }

    @Test
    void testCapitalGainCalculationNegative() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("100000.00"));

        BigDecimal salePrice = new BigDecimal("80000.00");
        BigDecimal gain = property.getCapitalGain(salePrice);

        assertEquals(new BigDecimal("-20000.00"), gain);
    }

    @Test
    void testCapitalGainWithNullSalePrice() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("100000.00"));

        BigDecimal gain = property.getCapitalGain(null);

        assertEquals(BigDecimal.ZERO, gain);
    }

    @Test
    void testCapitalGainWithNullPurchasePrice() {
        Property property = new Property();
        property.setPurchasePrice(null);

        BigDecimal gain = property.getCapitalGain(new BigDecimal("150000.00"));

        assertEquals(BigDecimal.ZERO, gain);
    }

    @Test
    void testToString() {
        Property property = new Property();
        property.setDescription("House");

        String result = property.toString();

        assertTrue(result.contains("Property"));
        assertTrue(result.contains("House"));
        assertTrue(result.contains(property.getId()));
    }
}