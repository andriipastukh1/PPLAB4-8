package ui.commands;

import service.TaxService;
import java.util.Scanner;

public class SaveCommand implements Command {
    private final TaxService svc = TaxService.getInstance();





    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);


        System.out.print("Enter file path to save : ");
        String path = sc.nextLine().trim();


        if (path.isEmpty()) svc.saveDefault();
        else svc.save(path);
        System.out.println("Saved.");
        util.AppLogger.LOGGER.info( "\"Saved.");

    }

    @Override
    public String getDesc() {
        return "Save Command";
    }
}
