package service;

import model.Person;
import model.TaxPayment;

import java.math.BigDecimal;
import java.util.List;

public class TaxReportGenerator {

    public String generateReport(List<Person> persons, TaxCalculator calc, int year) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TAX REPORT for year ").append(year).append(" ===\n");
        for (Person p : persons) {
            sb.append(p.getFullName()).append("\n");


            sb.append(" Total income: ").append(p.getTotalAnnualIncome(year)).append("\n");





            sb.append(" Taxes:\n");
            for (TaxPayment t : calc.calculateAllTaxes(p, year)) {
                sb.append("  - ").append(t.getTaxCategory()).append(": ").append(t.getAmount()).append(" (").append(t.getReason()).append(")\n");
            }
            sb.append(" TOTAL: ").append(calc.calculateTotalTax(p, year)).append("\n\n");
        }
        return sb.toString();
    }

    public void generateReceipt(TaxPayment payment) {
        System.out.println("=== RECEIPT ===");
        System.out.println("Amount: " + payment.getAmount());


        System.out.println("For: " + payment.getReason());
        System.out.println("Category: " + payment.getTaxCategory());
    }
}
