package tests.model;

import org.junit.jupiter.api.Test;
import model.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PropertySaleIncomeTest {

    @Test
    void testNoArgConstructor() {
        PropertySaleIncome income = new PropertySaleIncome();

        assertNotNull(income.getId());
        assertNull(income.getPropertySold());
        assertNull(income.getSalePrice());
        assertNull(income.getDate());
    }

    @Test
    void testFullConstructor() {
        Property property = new Property();
        property.setDescription("Apartment");
        BigDecimal salePrice = new BigDecimal("100000.00");
        LocalDate date = LocalDate.of(2025, 8, 1);
        String note = "Sold flat";

        PropertySaleIncome income = new PropertySaleIncome(property, salePrice, date, note);

        assertEquals(property, income.getPropertySold());
        assertEquals(salePrice, income.getSalePrice());
        assertEquals(date, income.getDate());
        assertEquals(note, income.getNote());
    }

    @Test
    void testCalculateCapitalGainPositive() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("50000.00"));

        BigDecimal salePrice = new BigDecimal("75000.00");
        PropertySaleIncome income = new PropertySaleIncome(property, salePrice, LocalDate.now(), "Gain");

        assertEquals(new BigDecimal("25000.00"), income.calculateCapitalGain());
    }

    @Test
    void testCalculateCapitalGainNegative() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("50000.00"));

        BigDecimal salePrice = new BigDecimal("40000.00");
        PropertySaleIncome income = new PropertySaleIncome(property, salePrice, LocalDate.now(), "Loss");

        assertEquals(new BigDecimal("-10000.00"), income.calculateCapitalGain());
    }

    @Test
    void testCalculateCapitalGainZero() {
        Property property = new Property();
        property.setPurchasePrice(new BigDecimal("50000.00"));

        BigDecimal salePrice = new BigDecimal("50000.00");
        PropertySaleIncome income = new PropertySaleIncome(property, salePrice, LocalDate.now(), "Zero");

        assertEquals(new BigDecimal("0.00"), income.calculateCapitalGain());
    }

    @Test
    void testCalculateCapitalGainWithNullProperty() {
        PropertySaleIncome income = new PropertySaleIncome(null, new BigDecimal("1000.00"), LocalDate.now(), "n");
        assertEquals(BigDecimal.ZERO, income.calculateCapitalGain());
    }

    @Test
    void testCalculateCapitalGainWithNullSalePrice() {
        Property property = new Property();
        property.setPurchasePrice(BigDecimal.TEN);

        PropertySaleIncome income = new PropertySaleIncome(property, null, LocalDate.now(), "n");
        assertEquals(BigDecimal.ZERO, income.calculateCapitalGain());
    }

    @Test
    void testCalculateCapitalGainWithNullPurchasePrice() {
        Property property = new Property();
        property.setPurchasePrice(null);

        PropertySaleIncome income = new PropertySaleIncome(property, new BigDecimal("1000.00"), LocalDate.now(), "n");
        assertEquals(BigDecimal.ZERO, income.calculateCapitalGain());
    }

    @Test
    void testGetTaxCategory() {
        PropertySaleIncome income = new PropertySaleIncome();
        assertEquals(TaxCategory.CAPITAL_GAINS, income.getTaxCategory());
    }
}