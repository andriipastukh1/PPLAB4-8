package ui.commands;

import model.Person;
import model.TaxPayment;
import service.TaxCalculator;
import service.TaxService;

import java.util.List;
import java.util.Scanner;

public class RegisterPaymentCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final TaxCalculator calc = svc.getCalculator();

    @Override
    public void execute() {


        Scanner sc = new Scanner(System.in);


        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();
        Person p = svc.findPersonByFullName(full);
        if (p == null) { System.out.println("Person not found."); return; }
        System.out.print("Enter year: ");


        int year = Integer.parseInt(sc.nextLine().trim());


        List<TaxPayment> taxes = calc.calculateAllTaxes(p, year);


        if (taxes.isEmpty()) { System.out.println("No taxes found."); return; }
        System.out.println("Taxes:");


        for (int i = 0; i < taxes.size(); i++) System.out.println(i + ". " + taxes.get(i).getTaxCategory() + " " + taxes.get(i).getAmount());
        System.out.print("Enter index to mark as paid (or 'all'): ");

        String s = sc.nextLine().trim();


        if (s.equalsIgnoreCase("all")) {


            for (TaxPayment t : taxes) { t.markAsPaid(); p.taxPayments.add(t); }


            System.out.println("Marked all as paid.");
        } else {
            int idx = Integer.parseInt(s);


            if (idx >= 0 && idx < taxes.size()) { TaxPayment t = taxes.get(idx); t.markAsPaid(); p.taxPayments.add(t); System.out.println("Marked paid."); }
            else System.out.println("Index out of range.");
        }
    }
}
