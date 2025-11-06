package ui;

import ui.commands.*;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Calculate taxes");
            System.out.println("2. Show report");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> new CalculateTaxesCommand().execute();
                case "2" -> new ShowReportCommand().execute();
                case "3" -> {
                    new ExitCommand().execute();
                    running = false;
                }
                default -> System.out.println("Error :) Invalid option!");


            }
        }
    }
}
