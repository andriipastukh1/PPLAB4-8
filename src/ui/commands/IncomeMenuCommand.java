package ui.commands;

import model.*;
import service.TaxService;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class IncomeMenuCommand implements Command {

    private final TaxService svc = TaxService.getInstance();
    private final Map<Integer, Command> menuItems = new TreeMap<>();

    public IncomeMenuCommand() {
        init();
    }

    private void init() {
        menuItems.put(1, new EnterIncomeCommand());

        menuItems.put(2, new Command() {
            @Override
            public void execute() {
                listIncomes(new Scanner(System.in));
            }

            @Override
            public String getDesc() {
                return "List incomes for person";
            }
        });

        menuItems.put(3, new Command() {
            @Override
            public void execute() {
                deleteIncome(new Scanner(System.in));
            }

            @Override
            public String getDesc() {
                return "Delete income (by index)";
            }
        });
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("\n--- INCOME MANAGEMENT ---");

            for (Map.Entry<Integer, Command> entry : menuItems.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue().getDesc());
            }
            System.out.println("0. Back to main menu");

            System.out.print("Choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();

                if (choice == 0) {
                    System.out.println("Returning to main menu...");
                    break;
                }

                Command cmd = menuItems.get(choice);
                if (cmd != null) {
                    cmd.execute();
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Error: Please enter a number.");
                sc.nextLine();
            }

        } while (choice != 0);
    }

    private void listIncomes(Scanner sc) {
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();

        Person p = svc.findPersonByFullName(full);
        if (p == null) {
            System.out.println("Person not found.");
            return;
        }
        if (p.incomes == null || p.incomes.isEmpty()) {
            System.out.println("No incomes for " + p.getFullName());
            return;
        }
        System.out.println("Incomes:");
        for (int i = 0; i < p.incomes.size(); i++) {
            Income inc = p.incomes.get(i);
            System.out.printf("%d) id=%s type=%s amount=%s date=%s note=%s\n",
                    i, inc.getId(), inc.getClass().getSimpleName(),
                    inc.getAmount() == null ? "0" : inc.getAmount().toPlainString(),
                    inc.getDate() == null ? "" : inc.getDate().toString(),
                    inc.getNote() == null ? "" : inc.getNote());
        }
    }

    private void deleteIncome(Scanner sc) {
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();

        Person p = svc.findPersonByFullName(full);
        if (p == null) {
            System.out.println("Person not found.");
            return;
        }
        if (p.incomes == null || p.incomes.isEmpty()) {
            System.out.println("No incomes to delete.");
            return;
        }

        listIncomes(sc);

        System.out.print("Enter index to delete: ");
        String sidx = sc.nextLine().trim();
        try {
            int idx = Integer.parseInt(sidx);
            if (idx < 0 || idx >= p.incomes.size()) {
                System.out.println("Index out of range.");
                return;
            }
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