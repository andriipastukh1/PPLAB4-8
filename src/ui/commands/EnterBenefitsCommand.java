package ui.commands;

import model.TaxBenefit;
import model.Person;
import service.CategoryService;
import service.TaxService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class EnterBenefitsCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final CategoryService catSvc = CategoryService.getInstance();

    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);



        String full = ui.InputUtils.readString(sc, "Enter person full name: ");


        Person p = svc.findPersonByFullName(full);


        if (p == null) { System.out.println("Person not found."); return; }




        System.out.println("Benefit categories:");
        List<String> subs = catSvc.getSubcategories("BENEFIT");


        for (int i = 0; i < subs.size(); i++) System.out.println((i+1) + ". " + subs.get(i));


        System.out.println("a. Add new benefit subcategory");



        String ch = ui.InputUtils.readString(sc, "Choose number or 'a': ");



        if (ch.equalsIgnoreCase("a")) {



            String name = ui.InputUtils.readString(sc, "New subcategory name: ");


            if (catSvc.addSubcategory("BENEFIT", name)) System.out.println("Added.");


            else System.out.println("Not added (exists).");
        }

        try {
            String bname = ui.InputUtils.readString(sc, "Benefit name (or choose subcategory): ");





            BigDecimal amount = ui.InputUtils.readMoney(sc, "Amount");

            boolean isPercent = ui.InputUtils.confirm(sc, "Is this a percent?");



            int f = ui.InputUtils.readInt(sc, "Valid from year: ");





            int t = ui.InputUtils.readInt(sc, "Valid to year: ");

            TaxBenefit b = new TaxBenefit(bname, amount, isPercent, f, t);
            svc.addBenefitToPerson(p, b);
            System.out.println("Benefit added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public String getDesc() {
        return "Enter Benefit";
    }
}