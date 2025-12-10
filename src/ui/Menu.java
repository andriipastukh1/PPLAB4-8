package ui;

import ui.commands.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;

public class Menu implements Command {
    private Map<Integer, Command> commands = new TreeMap<>();

    public Menu() {
        init();
    }

    protected void init() {
        commands.put(1, new IncomeMenuCommand());
        commands.put(2, new BenefitsMenuCommand());
        commands.put(3, new ImportDataCommand());
        commands.put(4, new TaxesMenuCommand());
        commands.put(7, new RegisterPaymentCommand());
        commands.put(10, new ShowAllDataCommand());
        commands.put(11, new SaveCommand());



    }

    public void showMainMenu() {
        System.out.println("\n==========================================");
        System.out.println("          TAX SYSTEM MAIN MENU            ");
        System.out.println("==========================================");

        for (Map.Entry<Integer, Command> entry : commands.entrySet()) {
            Integer key = entry.getKey();
            Command cmd = entry.getValue();
            System.out.println(key + ". " + cmd.getDesc());
        }

        System.out.println("------------------------------------------");
        System.out.println("0. Exit");
        System.out.println("==========================================");
    }

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            showMainMenu();
            System.out.print("Your Choice: ");

            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 0) {

                    System.out.println("Exiting system. Goodbye!");
                    isRunning = false;
                } else {
                    Command cmd = commands.get(choice);
                    if (cmd != null) {
                        cmd.execute();
                    } else {
                        System.out.println("Error: Invalid choice! Please try again.");
                    }
                }
            } else {
                System.out.println("Error: Please enter a number.");
                sc.nextLine();
            }
        }
    }


    @Override
    public String getDesc() {
        return "Main Menu";
    }
}