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
        System.out.print("Enter person full name: ");
        String full = sc.nextLine().trim();



        Person p = svc.findPersonByFullName(full);


        if (p == null) { System.out.println("Person not found."); return; }

        System.out.println("Benefit categories:");


        List<String> subs = catSvc.getSubcategories("BENEFIT");
        for (int i = 0; i < subs.size(); i++) System.out.println((i+1) + ". " + subs.get(i));
        System.out.println("a. Add new benefit subcategory");


        System.out.print("Choose number or 'a': ");


        String ch = sc.nextLine().trim();


        if (ch.equalsIgnoreCase("a")) {


            System.out.print("New subcategory name: ");


            String name = sc.nextLine().trim();
            if (catSvc.addSubcategory("BENEFIT", name)) System.out.println("Added.");
            else System.out.println("Not added (exists).");


            subs = catSvc.getSubcategories("BENEFIT");


        }

        try {
            System.out.print("Benefit name (or choose subcategory): ");


            String bname = sc.nextLine().trim();

            if (bname.isEmpty()) {


                System.out.println("No benefit name, abort.");
                return;
            }


            System.out.print("Amount (if percent, enter percent number): ");


            BigDecimal amount = new BigDecimal(sc.nextLine().trim());
            System.out.print("Is percent? true/false: ");


            boolean isPercent = Boolean.parseBoolean(sc.nextLine().trim());
            System.out.print("Valid from year: ");


            int f = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Valid to year: ");


            int t = Integer.parseInt(sc.nextLine().trim());



            TaxBenefit b = new TaxBenefit(bname, amount, isPercent, f, t);
            svc.addBenefitToPerson(p, b);

            System.out.println("Benefit added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
