package ui.commands;

import model.Person;
import service.TaxCalculator;
import service.TaxService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class ShowAnalyticsCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final TaxCalculator calc = svc.getCalculator();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- TAX ANALYTICS ---");
        String full = ui.InputUtils.readString(sc, "Enter person full name: ");
        Person p = svc.findPersonByFullName(full);

        if (p == null) { System.out.println("Person not found."); return; }

        int year = ui.InputUtils.readInt(sc, "Enter tax year for analysis: ");

        BigDecimal totalIncome = p.getTotalAnnualIncome(year);
        BigDecimal taxableIncome = p.getTaxableIncome(year);


        BigDecimal totalTax = calc.calculateTotalTax(p, year);
        BigDecimal netIncome = totalIncome.subtract(totalTax);

        double effectiveRate = 0.0;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            effectiveRate = totalTax.divide(totalIncome, 4, RoundingMode.HALF_UP).doubleValue() * 100.0;
        }

        System.out.println("\n" + "=".repeat(40));
        System.out.printf(" ANALYTICS FOR: %s (%d)\n", p.getFullName(), year);


        System.out.println("=".repeat(40));
        System.out.printf(" Total Gross Income:  %15s\n", format(totalIncome));

        System.out.printf(" - Applicable Benefits: %13s\n", format(totalIncome.subtract(taxableIncome)));


        System.out.printf(" Taxable Income:      %15s\n", format(taxableIncome));


        System.out.println("-".repeat(40));


        System.out.printf(" TOTAL TAX TO PAY:    %15s\n", format(totalTax));

        System.out.printf(" Effective Tax Rate:  %14.2f%%\n", effectiveRate);


        System.out.println("=".repeat(40));

        System.out.printf(" NET INCOME (Pocket): %15s\n", format(netIncome));


        System.out.println("=".repeat(40));
    }

    private String format(BigDecimal val) {
        if (val == null) return "0.00";
        return String.format("%.2f", val);
    }


    @Override
    public String getDesc() {
        return "Show Analytics";
    }
}