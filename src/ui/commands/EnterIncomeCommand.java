package ui.commands;

import model.*;
import service.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;






public class EnterIncomeCommand implements Command {
    private final TaxService svc = TaxService.getInstance();
    private final CategoryService catSvc = CategoryService.getInstance();

    @Override
    public void execute() {



        Scanner sc = new Scanner(System.in);



        System.out.print("Enter person full name (First Last): ");
        String full = sc.nextLine().trim();
        Person p = svc.findPersonByFullName(full);
        if (p == null) {



            System.out.print("Person not found. Create new? y/n: ");






            String ans = sc.nextLine().trim();



            if (!ans.equalsIgnoreCase("y")) return;


//            String[] parts = full.split(" ", 2);
//            p = new Person(parts.length > 0 ? parts[0] : full, parts.length > 1 ? parts[1] : "");
//            svc.addPerson(p);
//            System.out.println("Created: " + p.getFullName());
            String[] parts = full.split(" ", 2);
            p = new Person(parts.length > 0 ? parts[0] : full, parts.length > 1 ? parts[1] : "");
            svc.addPerson(p);
            System.out.println("Created: " + p.getFullName());
        }

        // Show cat
        System.out.println("Income categories:");
        List<String> subs = catSvc.getSubcategories("INCOME");
        for (int i = 0; i < subs.size(); i++) System.out.println((i+1) + ". " + subs.get(i));
        System.out.println("a. Add new subcategory");


        System.out.print("Choose subcategory number or 'a': ");


        String subChoice = sc.nextLine().trim();


        if (subChoice.equalsIgnoreCase("a")) {


            System.out.print("Enter new subcategory name: ");
            String name = sc.nextLine().trim();
            if (catSvc.addSubcategory("INCOME", name)) System.out.println("Added subcategory.");
            else System.out.println("Could not add subcategory or already exists.");
            subs = catSvc.getSubcategories("INCOME");
        }



        System.out.println("Income types: 1=Job 2=Gift 3=Foreign 4=PropertySale");


        System.out.print("Type: ");


        String type = sc.nextLine().trim();

        try {
            System.out.print("Amount: ");


            BigDecimal amt = new BigDecimal(sc.nextLine().trim());


            System.out.print("Date (YYYY-MM-DD) or empty: ");

            String ds = sc.nextLine().trim();

            LocalDate date = ds.isEmpty() ? LocalDate.now() : LocalDate.parse(ds);



            Income income = null;
            switch (type) {
                case "1":


                    System.out.print("Employer: ");


                    String emp = sc.nextLine();
                    income = new JobIncome(amt, date, emp, "job");


                    break;
                case "2":
                    System.out.print("Giver: ");


                    String giver = sc.nextLine();
                    System.out.print("Monetary? true/false: ");



                    boolean mon = Boolean.parseBoolean(sc.nextLine().trim());


                    income = new GiftIncome(amt, date, giver, mon, "gift");
                    break;
                case "3":
                    System.out.print("Origin country: ");


                    String c = sc.nextLine();
                    income = new ForeignIncome(amt, date, c, "foreign");
                    break;
                case "4":
                    Property prop = new Property();
                    System.out.print("Purchase price: ");


                    prop.setPurchasePrice(new BigDecimal(sc.nextLine().trim()));

                    System.out.print("Sale price: ");

                    BigDecimal sale = new BigDecimal(sc.nextLine().trim());

                    prop.setEstimatedValue(sale);


                    prop.setDescription("Sold property");


                    p.addProperty(prop);
                    income = new PropertySaleIncome(prop, sale, date, "sale");
                    break;
                default:
                    System.out.println("Unknown type");
                    return;
            }


            svc.addIncomeToPerson(p, income);
            System.out.println("Income added for " + p.getFullName());

        } catch (Exception ex) {
            System.out.println("Error parsing input: " + ex.getMessage());
        }
    }
}
