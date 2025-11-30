package ui.commands;

import service.TaxReportGenerator;
import service.TaxCalculator;
import service.TaxService;

import java.util.Scanner;

public class ShowReportCommand implements Command {
    private final TaxService svc = TaxService.getInstance();


    private final TaxReportGenerator gen = new TaxReportGenerator();


    private final TaxCalculator calc = svc.getCalculator();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter year for report: ");


        int year = Integer.parseInt(sc.nextLine().trim());


        System.out.println(gen.generateReport(svc.getAllPersons(), calc, year));
    }
}
