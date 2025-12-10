package ui.commands;

import util.AppLogger;
import model.Person;
import model.TaxPayment;
import service.TaxCalculator;
import service.TaxService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class CalculateTaxesCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final TaxCalculator calc = svc.getCalculator();

    @Override

    public void execute() {

        AppLogger.LOGGER.info("User executing Calculate Taxes command.");


        Scanner sc = new Scanner(System.in);



        String full = ui.InputUtils.readString(sc, "Enter person full name: ");
        Person p = svc.findPersonByFullName(full);

        if (p == null) {


            AppLogger.LOGGER.warning("Calculate Taxes: Person not found '" + full + "'");
            System.out.println("Person not found.");


            return;
        }



        int year = ui.InputUtils.readInt(sc, "Enter year (YYYY): ");



        List<TaxPayment> taxes = calc.calculateAllTaxes(p, year);





        System.out.println("Taxes for " + p.getFullName() + " in " + year + ":");


        for (TaxPayment t : taxes) {


            System.out.println(" - " + t.getTaxCategory() + ": " + t.getAmount() + " (" + t.getReason() + ")");
        }



        BigDecimal total = calc.calculateTotalTax(p, year);


        System.out.println("Total tax: " + total);



        AppLogger.LOGGER.info("Calculated taxes for " + p.getFullName() + " (Year " + year + "). Total: " + total);

        BigDecimal totalIncome = p.getTotalAnnualIncome(year);




        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {


            double burden = total.divide(totalIncome, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100.0;


            System.out.printf("Tax burden: %.2f%%\n", burden);


        }
    }

    @Override
    public String getDesc() {


        return "Calculate Taxes";


    }
}