package ui;

import util.AppLogger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {

    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {


                AppLogger.LOGGER.warning("InputUtils: Invalid integer input: '" + input + "'");


                System.out.println("Error: Invalid input! Please enter a whole number.");
            }
        }
    }

    public static BigDecimal readMoney(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            try {
                input = input.replace(",", ".");
                BigDecimal value = new BigDecimal(input);

                if (value.compareTo(BigDecimal.ZERO) < 0) {



                    AppLogger.LOGGER.warning("InputUtils: Negative money input attempt: '" + input + "'");
                    System.out.println("Error: Amount cannot be negative.");
                    continue;
                }
                return value;

            } catch (NumberFormatException e) {



                AppLogger.LOGGER.warning("InputUtils: Invalid money format: '" + input + "'");
                System.out.println("Error: Invalid amount! Please enter a number (e.g. 100.50).");
            }
        }
    }

    public static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }



            AppLogger.LOGGER.warning("InputUtils: User entered empty string Prompt: " + prompt);
            System.out.println("Error: This field cannot be empty.");
        }
    }

    public static boolean confirm(Scanner sc, String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = sc.nextLine().trim();




        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    public static LocalDate readDate(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {



                AppLogger.LOGGER.warning("InputUtils Invalid date format input: '" + input + "'");
                System.out.println("Error date format (use YYYY-MM-DD)");
            }
        }




    }
}