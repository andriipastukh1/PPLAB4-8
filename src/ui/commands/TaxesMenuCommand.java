package ui.commands;

import model.Person;
import model.TaxPayment;
import service.TaxService;


import service.TaxCalculator;

import java.math.BigDecimal;


import java.util.List;
import java.util.Scanner;



public class TaxesMenuCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    public TaxesMenuCommand() {
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String choice;
        do {


            System.out.println("\n--- TAXES MENU ---");
            System.out.println("1. Calculate taxes for person");
            System.out.println("2. Show tax report for year");
            System.out.println("3. Search taxes by amount range");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            choice = sc.nextLine().trim();

            switch (choice) {


                case "1" -> calculateTaxes(sc);
                case "2" -> showTaxReport(sc);


                case "3" -> searchTaxesByRange(sc);
                case "0" -> System.out.println("Returning to main menu...");


                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (!choice.equals("0"));
    }

    private void calculateTaxes(Scanner sc) {
        System.out.print("Enter person full name: ");
        String fullname = sc.nextLine().trim();



        Person p = svc.findPersonByFullName(fullname);



        if (p == null) { System.out.println("Person not found."); return; }

        System.out.print("Enter year (YYYY): ");


        int year = Integer.parseInt(sc.nextLine().trim());

        TaxCalculator calc = svc.getCalculator();


        List<TaxPayment> payments = calc.calculateAllTaxes(p, year);



        System.out.println("Taxes for " + p.getFullName() + " in " + year + ":");
        for (TaxPayment t : payments) {


            p.addTaxPayment(t);

            System.out.println(" - " + t.getTaxCategory() + ": " + t.getAmount());
        }

        svc.saveDefault();
    }

    private void showTaxReport(Scanner sc) {
        System.out.print("Enter year for report: ");


        int year = Integer.parseInt(sc.nextLine().trim());

        List<Person> persons = svc.getAllPersons();
        System.out.println("=== TAX REPORT for year " + year + " ===");


        for (Person p : persons) {


            System.out.println(p.getFullName());



            List<TaxPayment> taxes = p.getTaxPaymentsForYear(year);

            if (taxes.isEmpty()) System.out.println("  No recorded taxes.");


            else taxes.forEach(t -> System.out.println("  - " + t.getTaxCategory() + ": " + t.getAmount()));
        }
    }

    private void searchTaxesByRange(Scanner sc) {


        System.out.print("Enter person full name (leave empty for all): ");
        String fullname = sc.nextLine().trim();



        System.out.print("Enter minimum amount: ");


        BigDecimal min = new BigDecimal(sc.nextLine().trim());

        System.out.print("Enter maximum amount: ");


        BigDecimal max = new BigDecimal(sc.nextLine().trim());

        List<Person> persons;
        if (fullname.isEmpty()) {
            persons = svc.getAllPersons();



        } else {

            Person p = svc.findPersonByFullName(fullname);
            persons = (p != null) ? List.of(p) : List.of();



        }


        for (Person p : persons) {
            System.out.println("Taxes for " + p.getFirstName() + ":");

            List<TaxPayment> taxes = p.getTaxPaymentsInRange(min, max);

            if (taxes.isEmpty()) System.out.println("  No taxes in this range.");
            else taxes.forEach(t -> System.out.println("  - " + t.getTaxCategory() + ": " + t.getAmount()));
        }
    }
}