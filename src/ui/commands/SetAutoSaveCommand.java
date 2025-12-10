package ui.commands;

import service.TaxService;
import java.util.Scanner;

public class SetAutoSaveCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enable auto save y/n: ");




        String yn = sc.nextLine().trim();




        if (!yn.equalsIgnoreCase("y")) { svc.setAutoSaveBatch(null, false); System.out.println("Auto-save disabled."); return; }
        System.out.print("Enter bat file path to append commands:: ");
        util.AppLogger.LOGGER.info( "\"Enter bat file path to append commands:: ");


//        if (!yn.equalsIgnoreCase("y")) { svc.setAutoSaveBatch(null, false); System.out.println("Auto-save disabled."); return; }
//        System.out.print("Enter batch file path to append commands (e.g. operations.bat): ");





        String path = sc.nextLine().trim();
        svc.setAutoSaveBatch(path, true);
        System.out.println("Auto-save enabled; " + path);
        util.AppLogger.LOGGER.info( "Auto-save enabled; " + path);

    }
    @Override
    public String getDesc() {
        return "Auto save Menegement";
    }


}
