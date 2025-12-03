package ui;

import ui.commands.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu implements ui.commands.Command {
    private Map<Integer, ui.commands.Command> commands = new HashMap<>();

    public Menu() { init(); }

    protected void init() {

        commands.put(1, new IncomeMenuCommand());
        commands.put(2, new BenefitsMenuCommand());
        commands.put(3, new ImportDataCommand());
        commands.put(4, new TaxesMenuCommand());
        commands.put(7, new RegisterPaymentCommand());
//        commands.put(8, new SetAutoSaveCommand());
//        commands.put(9, new ImportBatchCommand());
        commands.put(10, new ShowAllDataCommand());
        commands.put(11, new SaveCommand());
        commands.put(0, new ExitCommand());
    }

    public void showMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("          TAX SYSTEM MAIN MENU            ");
        System.out.println("==========================================");

        System.out.println("--- Categories ---");
        System.out.println("1. Income (manage incomes)");
        System.out.println("2. Benefits (manage benefits)");
        System.out.println("3. Import Data from File (.ser)");

        System.out.println("\n--- Calculations & Rates ---");
        System.out.println("4. Calculate Tax Payments");


        System.out.println("\n--- Payments ---");
        System.out.println("7. Register Payment");

        System.out.println("\n--- Utilities ---");
//        System.out.println("8. Autosave");
//        System.out.println("9. Import Batch Script (.bat)");
        System.out.println("10. Show All Data");
        System.out.println("11. Save to file");

        System.out.println("\n0. Exit");
        System.out.print("------------------------------------------\n");
    }

    public void handleUserChoice(int choice) {
        ui.commands.Command cmd = commands.get(choice);
        if (cmd != null) cmd.execute();
        else System.out.println("Error: Invalid choice! Please try again.");
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        do {
            showMainMenu();
            System.out.print("Your Choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();
                handleUserChoice(choice);
            } else {
                System.out.println("Error: Please enter a number.");
                sc.nextLine();
            }
        } while (choice != 0);
        System.out.println("Exiting system. Goodbye!");
    }
}
