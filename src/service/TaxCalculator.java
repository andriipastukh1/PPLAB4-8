package service;

import util.AppLogger; // <--- Імпорт логгера
import model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class TaxCalculator {

    private Map<TaxCategory, BigDecimal> rates = new EnumMap<>(TaxCategory.class);

    public TaxCalculator() {
        loadRates();
    }

    private void loadRates() {
        for (TaxCategory tc : TaxCategory.values()) {


            rates.put(tc, BigDecimal.valueOf(tc.getDefaultRate()));


        }
        AppLogger.LOGGER.info("TaxCalculator: Default tax rates");




        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("tax_rates.properties")) {


            props.load(fis);


            for (TaxCategory tc : TaxCategory.values()) {


                String val = props.getProperty(tc.name());


                if (val != null && !val.isBlank()) {


                    try {
                        rates.put(tc, new BigDecimal(val.trim()));


                    } catch (NumberFormatException e) {







                        AppLogger.LOGGER.warning("TaxCalculator: Invalid number format in 'tax_rates.properties' for " + tc.name() + ": " + val);


                    }


                }


            }

            AppLogger.LOGGER.info("TaxCalculator: External rates loaded from 'tax_rates.properties'.");


        } catch (IOException e) {


            AppLogger.LOGGER.warning("TaxCalculator: 'tax_rates.properties' not found or unreadable. Using default rates.");
        }
    }

    public Map<TaxCategory, BigDecimal> getRates() {



        return rates;


    }

    public void updateRate(TaxCategory cat, BigDecimal newRate) {


        if (cat != null && newRate != null) {


            rates.put(cat, newRate);





            AppLogger.LOGGER.info("TaxCalculator: Rate UPDATED for " + cat + " -> " + newRate);
        }
    }

    public List<TaxPayment> calculateAllTaxes(Person person, int year) {
        List<TaxPayment> payments = new ArrayList<>();
        if (person == null) return payments;

        AppLogger.LOGGER.info("TaxCalculator: Starting calculation for " + person.getFullName() + " (Year: " + year + ")");

        for (Income inc : person.incomes) {
            if (!inc.isAnnual(year)) continue;

            BigDecimal tax = calculateTaxForIncome(inc, person, year);

            if (tax.compareTo(BigDecimal.ZERO) > 0) {
                TaxPayment t = new TaxPayment(tax, year, inc.getClass().getSimpleName() + " (" + inc.getNote() + ")", inc.getTaxCategory());
                payments.add(t);
            }
        }

        for (Property p : person.properties) {
            BigDecimal est = p.getEstimatedValue();
            if (est != null) {


                BigDecimal rate = rates.getOrDefault(TaxCategory.PROPERTY_TAX, BigDecimal.ZERO);
                BigDecimal tax = est.multiply(rate);

                if (tax.compareTo(BigDecimal.ZERO) > 0) {


                    payments.add(new TaxPayment(tax, year, "Property tax " + p.getDescription(), TaxCategory.PROPERTY_TAX));
                }
            }
        }

        payments.sort(Comparator.comparing(TaxPayment::getAmount));



        AppLogger.LOGGER.info("TaxCalculator: Calculation finished. Generated " + payments.size() + " tax entries.");
        return payments;




    }



    public BigDecimal calculateTaxForIncome(Income inc, Person person, int year) {
        if (inc == null || inc.getAmount() == null) return BigDecimal.ZERO;


        TaxCategory cat = inc.getTaxCategory();





        BigDecimal gross = inc.getAmount();

        switch (cat) {
            case INCOME_TAX:


                BigDecimal benefitsReduction = BigDecimal.ZERO;


                for (TaxBenefit b : person.benefits) {


                    if (b.isApplicable(year)) {
                        BigDecimal reduction = b.applyBenefit(gross);


                        benefitsReduction = benefitsReduction.add(reduction);




                        AppLogger.LOGGER.info("TaxCalculator: Applied benefit '" + b.getDescription() + "' -> reduced base by " + reduction);
                    }
                }
                BigDecimal taxable = gross.subtract(benefitsReduction);
                if (taxable.compareTo(BigDecimal.ZERO) <= 0) taxable = BigDecimal.ZERO;



                BigDecimal incomeRate = rates.getOrDefault(TaxCategory.INCOME_TAX, BigDecimal.ZERO);
                BigDecimal incomeTax = taxable.multiply(incomeRate);





                BigDecimal milRate = rates.getOrDefault(TaxCategory.MILITARY_TAX, BigDecimal.ZERO);
                BigDecimal military = gross.multiply(milRate);

                return incomeTax.add(military);

            case GIFT_TAX:


                return gross.multiply(rates.getOrDefault(TaxCategory.GIFT_TAX, BigDecimal.ZERO));

            case FOREIGN_INCOME_TAX:


                return gross.multiply(rates.getOrDefault(TaxCategory.FOREIGN_INCOME_TAX, BigDecimal.ZERO));



            case ROYALTY_TAX:
                return gross.multiply(rates.getOrDefault(TaxCategory.ROYALTY_TAX, BigDecimal.ZERO));

            case CAPITAL_GAINS:
                if (inc instanceof PropertySaleIncome) {
                    BigDecimal gain = ((PropertySaleIncome) inc).calculateCapitalGain();


                    if (gain.compareTo(BigDecimal.ZERO) <= 0) {


                        AppLogger.LOGGER.info("TaxCalculator: No capital gain for property sale (Gain: " + gain + "). Tax is 0.");


                        return BigDecimal.ZERO;


                    }
                    return gain.multiply(rates.getOrDefault(TaxCategory.CAPITAL_GAINS, BigDecimal.ZERO));
                } else {
                    return gross.multiply(rates.getOrDefault(TaxCategory.CAPITAL_GAINS, BigDecimal.ZERO));


                }






            default:
                return BigDecimal.ZERO;


        }
    }

    public BigDecimal calculateTotalTax(Person person, int year) {


        List<TaxPayment> list = calculateAllTaxes(person, year);



        BigDecimal sum = BigDecimal.ZERO;
        for (TaxPayment t : list) sum = sum.add(t.getAmount());


        return sum;
    }


    public List<TaxPayment> sortTaxesByAmount(List<TaxPayment> taxes, boolean ascending) {
        taxes.sort(Comparator.comparing(TaxPayment::getAmount));


        if (!ascending) Collections.reverse(taxes);


        return taxes;
    }



    public List<TaxPayment> findTaxesInRange(Person person, int year, BigDecimal min, BigDecimal max) {
        List<TaxPayment> all = calculateAllTaxes(person, year);


        List<TaxPayment> out = new ArrayList<>();


        for (TaxPayment t : all) {


            if ((min == null || t.getAmount().compareTo(min) >= 0) && (max == null || t.getAmount().compareTo(max) <= 0)) {
                out.add(t);
            }
        }
        return out;
    }
}