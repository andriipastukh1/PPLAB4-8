package ui;

import java.util.*;
import ui.commands.*;






public class Menu {
    private Map<Integer, Command> commands = new HashMap<>();

    public Menu() {
        commands.put(1, new ShowMainMenuCommand());

        commands.put(2, new CalculateTaxesCommand());
        commands.put(3, new ShowReportCommand());

        commands.put(0, new ExitCommand());



    }

    public void showMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. Show menu");

        System.out.println("2. Calculate taxes");


        System.out.println("3. Generate report");
        System.out.println("0. Exit");
    }

    public void handleUserChoice(int choice) {
        Command cmd = commands.get(choice);
        if (cmd != null) cmd.execute();
        else System.out.println("Error wrong chiose!");
    }
}
