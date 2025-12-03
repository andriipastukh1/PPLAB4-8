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



        System.out.println("\n--- Current Tax Rates ---");
        for (Map.Entry<TaxCategory, BigDecimal> e : rates.entrySet()) {


            System.out.println(e.getKey().name() + " = " + e.getValue());


        }



        if (!ui.InputUtils.confirm(sc, "Do you want to update a rate?")) return;



        String catName = ui.InputUtils.readString(sc, "Enter category name (e.g. INCOME_TAX): ");



        try {
            TaxCategory tcat = TaxCategory.valueOf(catName.toUpperCase());


            BigDecimal newRate = ui.InputUtils.readMoney(sc, "Enter new rate as decimal (e.g. 0.18): ");

            calc.updateRate(tcat, newRate);


            System.out.println("Updated " + tcat + " -> " + newRate);


        } catch (IllegalArgumentException ex) {


            System.out.println("Error: Invalid category name.");


        } catch (Exception ex) {


            System.out.println("Error: " + ex.getMessage());
        }
    }
}