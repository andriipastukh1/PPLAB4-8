package ui.commands;

import model.*;
import service.TaxService;





import java.util.List;
import java.util.Scanner;



public class IncomeMenuCommand implements Command {




    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        String choice;
        do {
            System.out.println("\n--- INCOME MANAGEMENT ---");
            System.out.println("1. Add income");


            System.out.println("2. List incomes for person");


            System.out.println("3. Delete income (by index)");


            System.out.println("4. Back to main menu");
            System.out.print("Choice: ");
            choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    new EnterIncomeCommand().execute();
                    break;
                case "2":
                    listIncomes(sc);
                    break;
                case "3":
                    deleteIncome(sc);
                    break;
                case "4":
                    // go back
                    System.out.println("Returning to main menu...");
                    break;
                default:


                    System.out.println("Invalid choice. Try again.");
            }
        } while (!choice.equals("4"));
    }





    private void listIncomes(Scanner sc) {
        System.out.print("Enter person full name: ");


        String full = sc.nextLine().trim();


        Person p = svc.findPersonByFullName(full);
        if (p == null) { System.out.println("Person not found."); return; }
        if (p.incomes == null || p.incomes.isEmpty()) {


            System.out.println("No incomes for " + p.getFullName());
            return;
        }
        System.out.println("Incomes:");
        for (int i = 0; i < p.incomes.size(); i++) {
            Income inc = p.incomes.get(i);


            System.out.printf("%d) id=%s type=%s amount=%s date=%s note=%s\n",
                    i, inc.getId(), inc.getClass().getSimpleName(),


                    inc.getAmount()==null?"0":inc.getAmount().toPlainString(),


                    inc.getDate()==null?"":inc.getDate().toString(),


                    inc.getNote()==null?"":inc.getNote());
        }
    }

    private void deleteIncome(Scanner sc) {
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();


        Person p = svc.findPersonByFullName(full);
        if (p == null) { System.out.println("Person not found."); return; }


        if (p.incomes == null || p.incomes.isEmpty()) { System.out.println("No incomes to delete."); return; }
        listIncomes(sc);


        System.out.print("Enter index to delete: ");


        String sidx = sc.nextLine().trim();
        try {


            int idx = Integer.parseInt(sidx);
            if (idx < 0 || idx >= p.incomes.size()) { System.out.println("Index out of range."); return; }
            String incId = p.incomes.get(idx).getId();
            boolean removed = p.removeIncome(incId);
            if (removed) {


                svc.saveDefault();
                System.out.println("Income removed and data saved.");
            } else {
                System.out.println("Failed to remove income.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
        }
    }

    @Override
    public String getDesc() {
        return "Income Management (Add, List, Delete)";
    }



}
