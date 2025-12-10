package ui.commands;

import service.TaxService;

import java.util.Scanner;

public class ImportDataCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);


        System.out.print("Enter file path to load data (.ser): ");
        util.AppLogger.LOGGER.info("Enter file path to load data (.ser):");

        util.AppLogger.LOGGER.info(" ");

        String path = sc.nextLine().trim();
        util.AppLogger.LOGGER.info("User requested data import from: " + path);
        util.AppLogger.LOGGER.info(" User requested data import from: ");



        svc.load(path);
        System.out.println("Loaded persons: " + svc.getAllPersons().size());
        util.AppLogger.LOGGER.info("Loaded persons: ");

    }


    @Override
    public String getDesc() {
        return "Import Data";
    }
}
