package ui.commands;

import service.TaxReportGenerator;
import service.TaxCalculator;
import service.TaxService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ShowReportCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final TaxReportGenerator gen = new TaxReportGenerator();
    private final TaxCalculator calc = svc.getCalculator();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);


        int year = ui.InputUtils.readInt(sc, "Enter year for report: ");

        String reportText = gen.generateReport(svc.getAllPersons(), calc, year);

        System.out.println(reportText);

        if (ui.InputUtils.confirm(sc, "Do you want to save this report to a file?")) {


            String fileName = ui.InputUtils.readString(sc, "Enter file name (for exep - report_2025.txt): ");



            try (FileWriter writer = new FileWriter(fileName)) {


                writer.write(reportText);


                System.out.println("Report successfully saved to " + fileName);


            } catch (IOException e) {


                System.out.println("Error saving file: " + e.getMessage());
            }
        }
    }
}