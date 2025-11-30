package ui.commands;

import service.TaxService;
import java.util.Scanner;

public class ImportBatchCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);


        System.out.print("Enter batch file path (.bat or .txt): ");




        String path = sc.nextLine().trim();
        if (path.isEmpty()) { System.out.println("No path entered."); return; }
        svc.processBatchFile(path);
    }
}
