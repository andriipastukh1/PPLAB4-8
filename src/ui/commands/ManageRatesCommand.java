package ui.commands;

import model.TaxCategory;
import service.TaxService;
import service.TaxCalculator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

public class ManageRatesCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final TaxCalculator calc = svc.getCalculator();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        Map<TaxCategory, BigDecimal> rates = calc.getRates();
        System.out.println("Current rates:");


        for (Map.Entry<TaxCategory, BigDecimal> e : rates.entrySet()) {


            System.out.println(e.getKey().name() + " = " + e.getValue());
        }


        System.out.print("Do you want to update a rate? y/n: ");


        if (!sc.nextLine().trim().equalsIgnoreCase("y")) return;
        System.out.print("Enter category name (e.g. INCOME_TAX): ");
        String cat = sc.nextLine().trim();
        try {


            TaxCategory tcat = TaxCategory.valueOf(cat);
            System.out.print("Enter new rate as decimal (e.g. 0.18): ");





            BigDecimal newRate = new BigDecimal(sc.nextLine().trim());


            calc.updateRate(tcat, newRate);


            System.out.println("Updated " + tcat + " -> " + newRate);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
