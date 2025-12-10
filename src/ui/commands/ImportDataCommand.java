package ui.commands;

import service.TaxService;

import java.util.Scanner;

public class ImportDataCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);


        System.out.print("Enter file path to load data (.ser): ");
        String path = sc.nextLine().trim();


        svc.load(path);
        System.out.println("Loaded persons: " + svc.getAllPersons().size());
    }


    @Override
    public String getDesc() {
        return "Import Data";
    }
}
