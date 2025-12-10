package ui.commands;

import util.AppLogger;
import java.util.logging.Level;
import model.Person;
import model.TaxPayment;
import service.TaxService;
import ui.InputUtils;
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
        AppLogger.LOGGER.info("User entered Taxes Management Menu.");
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\n--- TAXES MENU ---");
            System.out.println("1. Calculate taxes for person");
            System.out.println("2. Show tax report for year");
            System.out.println("3. Search taxes by amount range");
            System.out.println("4. Manage Tax Rates");
            System.out.println("5. Delete Tax Payment");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    AppLogger.LOGGER.info("User selected: Calculate Taxes");
                    calculateTaxes(sc);
                }
                case "2" -> {
                    AppLogger.LOGGER.info("User selected: Show Tax Report");
                    showTaxReport(sc);
                }
                case "3" -> {
                    AppLogger.LOGGER.info("User selected: Search Taxes by Range");
                    searchTaxesByRange(sc);
                }
                case "4" -> {
                    AppLogger.LOGGER.info("User selected: Manage Tax Rates");
                    new ManageRatesCommand().execute();
                }
                case "5" -> {
                    AppLogger.LOGGER.info("User selected: Delete Tax Payment");
                    deleteTaxPayment(sc);
                }
                case "0" -> {
                    AppLogger.LOGGER.info("Returning to main menu from Taxes Menu.");
                    System.out.println("Returning to main menu...");
                }
                default -> {
                    AppLogger.LOGGER.warning("Invalid choice in Taxes Menu: " + choice);
                    System.out.println("Invalid choice. Try again.");
                }
            }
        } while (!choice.equals("0"));
    }

    private void deleteTaxPayment(Scanner sc) {
        try {
            String fullname = InputUtils.readString(sc, "Enter person full name: ");
            Person p = svc.findPersonByFullName(fullname);



            if (p == null) {
                AppLogger.LOGGER.warning("Delete Tax: Person not found '" + fullname + "'");
                System.out.println("Person not found.");


                return;
            }

            int year = InputUtils.readInt(sc, "Enter year: ");
            List<TaxPayment> taxes = p.getTaxPaymentsForYear(year);



            if (taxes.isEmpty()) {
                AppLogger.LOGGER.info("Delete Tax: No taxes found for " + fullname + " in " + year);
                System.out.println("No taxes found for " + year);


                return;
            }

            System.out.println("--- Taxes for deletion ---");
            for (int i = 0; i < taxes.size(); i++) {


                TaxPayment t = taxes.get(i);


                System.out.println(i + ". " + t.getTaxCategory() + ": " + t.getAmount());
            }

            int index = InputUtils.readInt(sc, "Enter number to delete: ");

            if (index >= 0 && index < taxes.size()) {


                TaxPayment toDelete = taxes.get(index);





                if (InputUtils.confirm(sc, "Are you sure you want to delete this tax?")) {
                    if (p.removeTaxPayment(toDelete)) {


                        AppLogger.LOGGER.info("Tax deleted for " + fullname + ": " + toDelete.getAmount() + " (" + toDelete.getTaxCategory() + ")");
                        System.out.println("Tax deleted successfully.");


                        svc.saveDefault();
                    } else {


                        AppLogger.LOGGER.warning("Delete Tax: Failed to remove tax object.");
                        System.out.println("Error deleting tax.");


                    }
                } else {


                    System.out.println("Deletion cancelled.");


                }
            } else {


                AppLogger.LOGGER.warning("Delete Tax: Invalid index " + index);
                System.out.println("Invalid number.");


            }
        } catch (Exception e) {


            AppLogger.LOGGER.log(Level.SEVERE, "Error in deleteTaxPayment", e);


        }
    }

    private void calculateTaxes(Scanner sc) {


        try {
            String fullname = InputUtils.readString(sc, "Enter person full name: ");
            Person p = svc.findPersonByFullName(fullname);



            if (p == null) {
                AppLogger.LOGGER.warning("Calculate Taxes: Person not found '" + fullname + "'");
                System.out.println("Person not found.");


                return;


            }



            int year = InputUtils.readInt(sc, "Enter year (YYYY): ");


            TaxCalculator calc = svc.getCalculator();


            List<TaxPayment> payments = calc.calculateAllTaxes(p, year);





            System.out.println("Taxes for " + p.getFullName() + " in " + year + ":");


            for (TaxPayment t : payments) {


                p.addTaxPayment(t);


                System.out.println(" - " + t.getTaxCategory() + ": " + t.getAmount());


            }



            AppLogger.LOGGER.info("Calculated and saved " + payments.size() + " tax entries for " + fullname);
            svc.saveDefault();



        } catch (Exception e) {


            AppLogger.LOGGER.log(Level.SEVERE, "Error in calculateTaxes", e);


        }
    }

    private void showTaxReport(Scanner sc) {
        try {
            int year = InputUtils.readInt(sc, "Enter year for report: ");


            List<Person> persons = svc.getAllPersons();
            System.out.println("=== TAX REPORT for year " + year + " ===");


            AppLogger.LOGGER.info("Generating Tax Report for year " + year);



            for (Person p : persons) {
                System.out.println(p.getFullName());


                List<TaxPayment> taxes = p.getTaxPaymentsForYear(year);



                if (taxes.isEmpty()) {


                    System.out.println("  No recorded taxes.");


                } else {


            taxes.forEach(t -> System.out.println("  - " + t.getTaxCategory() + ": " + t.getAmount()));
                }


            }


        } catch (Exception e) {


            AppLogger.LOGGER.log(Level.SEVERE, "Error in showTaxReport", e);


        }
    }

    private void searchTaxesByRange(Scanner sc) {
        try {
            System.out.print("Enter person full name (leave empty for all): ");


            String fullname = sc.nextLine().trim();



            BigDecimal min = InputUtils.readMoney(sc, "Enter minimum amount: ");


            BigDecimal max = InputUtils.readMoney(sc, "Enter maximum amount: ");



            AppLogger.LOGGER.info("Searching taxes. Name: '" + fullname + "', Range: " + min + " - " + max);



            List<Person> persons;


            if (fullname.isEmpty()) {


                persons = svc.getAllPersons();


            } else {
                Person p = svc.findPersonByFullName(fullname);


                if (p == null) {


                    System.out.println("Person not found.");
                    return;


                }
                persons = List.of(p);
            }



            for (Person p : persons) {
                System.out.println("Taxes for " + p.getFirstName() + ":");


                List<TaxPayment> taxes = p.getTaxPaymentsInRange(min, max);



                if (taxes.isEmpty()) {


                    System.out.println("  No taxes in this range.");


                } else {


                    taxes.forEach(t -> System.out.println("  - " + t.getTaxCategory() + ": " + t.getAmount()));
                }


            }



        } catch (Exception e) {



            AppLogger.LOGGER.log(Level.SEVERE, "Error in searchTaxesByRange", e);

        }
    }

    @Override
    public String getDesc() {
        return "Tax Management (Add, Pay, Delete)";
    }
}