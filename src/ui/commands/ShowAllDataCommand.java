package ui.commands;

import service.TaxService;
import model.*;
import java.util.List;

public class ShowAllDataCommand implements Command {
    private final TaxService svc = TaxService.getInstance();

    @Override
    public void execute() {
        List<Person> persons = svc.getAllPersons();
        if (persons == null || persons.isEmpty()) { System.out.println("No persons in the system."); return; }
        for (Person p : persons){


            System.out.println("--------------------------------------------------");
            System.out.println("Person: " + p.getFullName() + " (id=" + p.getId() + ")");



            System.out.println("Incomes:");
            if (p.incomes == null || p.incomes.isEmpty()) System.out.println("  (none)");
            else {
                for (Income inc : p.incomes) {
                    System.out.println("  - " + inc.getClass().getSimpleName() +
                            " | amount=" + (inc.getAmount()==null?"0":inc.getAmount()) +
                            " | date=" + (inc.getDate()==null?"":inc.getDate()) +
                            " | note=" + (inc.getNote()==null?"":inc.getNote()));


                    if (inc instanceof PropertySaleIncome) {


                        PropertySaleIncome psi = (PropertySaleIncome) inc;


                        System.out.println("      property purchase=" + (psi.getPropertySold()!=null && psi.getPropertySold().getPurchasePrice()!=null ? psi.getPropertySold().getPurchasePrice() : "N/A")
                                + " sale=" + (psi.getSalePrice()!=null?psi.getSalePrice():"N/A"));
                    }
                }
            }

            System.out.println("Properties:");


            if (p.properties == null || p.properties.isEmpty()) System.out.println("  (none)");
            else {



                for (Property prop : p.properties) {
                    System.out.println("  - " + (prop.getDescription()==null?"(no desc)":prop.getDescription())
                            + " | purchase=" + (prop.getPurchasePrice()==null?"N/A":prop.getPurchasePrice())


                            + " | estimated=" + (prop.getEstimatedValue()==null?"N/A":prop.getEstimatedValue()));

                }
            }

            System.out.println("Benefits:");
            if (p.benefits == null || p.benefits.isEmpty()) System.out.println("  (none)");


            else {
                for (TaxBenefit b : p.benefits) {


                    String percent = b.isPercent() ? "percent" : "fixed";


                    String amt = b.getAmount() == null ? "0" : b.getAmount().toPlainString();


                    System.out.println("  - " + b.getDescription() + " | value=" + amt + " (" + percent + ")"


                            + " | valid: " + b.getValidFromYear() + "-" + b.getValidToYear());
                }
            }




            System.out.println("Tax payments:");
            if (p.taxPayments == null || p.taxPayments.isEmpty()) System.out.println("  (none)");
            else {


                for (TaxPayment t : p.taxPayments) {


                    System.out.println("  - " + (t.getTaxCategory() == null ? "N/A" : t.getTaxCategory())
                            + " | amount=" + (t.getAmount()==null?"0":t.getAmount())
                            + " | year=" + t.getTaxYear()


                            + " | paid=" + t.isPaid());
                }
            }
        }
        System.out.println("--------------------------------------------------");
    }

    @Override
    public String getDesc() {
        return "Show All Info";
    }

}
