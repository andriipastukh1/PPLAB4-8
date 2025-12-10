package ui.commands;

import model.*;
import service.TaxService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BenefitsMenuCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\n--- BENEFITS MANAGEMENT ---");



            System.out.println("1. Add benefit");


            System.out.println("2. List benefits for person");


            System.out.println("3. Delete benefit (by index)");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");


            choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    // reuse existing EnterBenefitsCommand
                    new EnterBenefitsCommand().execute();


                    break;


                case "2":
                    listBenefits(sc);
                    break;


                case "3":
                    deleteBenefit(sc);
                    break;
                case "0":


                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!choice.equals("4"));
    }

    private void listBenefits(Scanner sc) {
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();
        Person p = svc.findPersonByFullName(full);



        if (p == null) { System.out.println("Person not found."); return; }
        if (p.benefits == null || p.benefits.isEmpty()) {
            System.out.println("No benefits for " + p.getFullName());
            return;
        }
        System.out.println("Benefits:");
        for (int i = 0; i < p.benefits.size(); i++) {
            TaxBenefit b = p.benefits.get(i);
            String amt = b.getAmount() == null ? "0" : b.getAmount().toPlainString();
            System.out.printf("%d) name=%s value=%s (%s) valid=%d-%d\n",
                    i, b.getDescription(), amt, b.isPercent() ? "percent" : "fixed",
                    b.getValidFromYear(), b.getValidToYear());
        }
    }

    private void deleteBenefit(Scanner sc) {
        System.out.print("Enter person full name: ");


        String full = sc.nextLine().trim();


        Person p = svc.findPersonByFullName(full);


        if (p == null) { System.out.println("Person not found."); return; }


        if (p.benefits == null || p.benefits.isEmpty()) { System.out.println("No benefits to delete."); return; }


        listBenefits(sc);
        System.out.print("Enter index to delete: ");


        String sidx = sc.nextLine().trim();
        try {
            int idx = Integer.parseInt(sidx);



            if (idx < 0 || idx >= p.benefits.size()) { System.out.println("Index out of range."); return; }
            TaxBenefit b = p.benefits.get(idx);


            p.removeBenefit(b.getDescription());


            svc.saveDefault();


            System.out.println("Benefit removed and data saved.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
        }
    }


    @Override
    public String getDesc() {
        return "Benefits";
    }
}
