package ui.commands;

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
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();
        Person p = svc.findPersonByFullName(full);
        if (p == null) { System.out.println("Person not found."); return; }
        System.out.print("Enter year (YYYY): ");
        int year = Integer.parseInt(sc.nextLine().trim());
        List<TaxPayment> taxes = calc.calculateAllTaxes(p, year);
        System.out.println("Taxes for " + p.getFullName() + " in " + year + ":");
        for (TaxPayment t : taxes) {
            System.out.println(" - " + t.getTaxCategory() + ": " + t.getAmount() + " (" + t.getReason() + ")");
        }
        BigDecimal total = calc.calculateTotalTax(p, year);
        System.out.println("Total tax: " + total);
        BigDecimal totalIncome = p.getTotalAnnualIncome(year);
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            double burden = total.divide(totalIncome, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100.0;
            System.out.printf("Tax burden: %.2f%%\n", burden);
        }
    }
}
