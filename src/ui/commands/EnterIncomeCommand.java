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



        String full = ui.InputUtils.readString(sc, "Enter person full name (First Last): ");


        Person p = svc.findPersonByFullName(full);

        if (p == null) {


            if (!ui.InputUtils.confirm(sc, "Person not found. Create new?")) {
                return;
            }

            String[] parts = full.split(" ", 2);
            p = new Person(parts.length > 0 ? parts[0] : full, parts.length > 1 ? parts[1] : "");
            svc.addPerson(p);
            System.out.println("Created: " + p.getFullName());
        }

        System.out.println("\n--- Income categories ---");


        List<String> subs = catSvc.getSubcategories("INCOME");


        for (int i = 0; i < subs.size(); i++) {


            System.out.println((i + 1) + ". " + subs.get(i));

        }
        System.out.println("a. Add new subcategory");

        String subChoice = ui.InputUtils.readString(sc, "Choose subcategory number or 'a': ");


        if (subChoice.equalsIgnoreCase("a")) {


            String name = ui.InputUtils.readString(sc, "Enter new subcategory name: ");


            if (catSvc.addSubcategory("INCOME", name)) {


                System.out.println("Added subcategory.");
            } else {
                System.out.println("Could not add subcategory or already exists.");


            }
            subs = catSvc.getSubcategories("INCOME");
        }

        System.out.println("\nIncome types: 1=Job, 2=Gift, 3=Foreign, 4=PropertySale");


        int type = ui.InputUtils.readInt(sc, "Type (enter number): ");


        try {
            BigDecimal amt = ui.InputUtils.readMoney(sc, "Enter amount: ");

            System.out.print("Date (YYYY-MM-DD) or empty for today: ");


            String ds = sc.nextLine().trim();




            LocalDate date;
            try {
                date = ds.isEmpty() ? LocalDate.now() : LocalDate.parse(ds);
            } catch (Exception e) {


                System.out.println("Invalid date format. Using today's date.");



                date = LocalDate.now();
            }

            Income income = null;






            switch (type) {
                case 1:


                    String emp = ui.InputUtils.readString(sc, "Employer: ");
                    income = new JobIncome(amt, date, emp, "job");


                    break;
                case 2:
                    String giver = ui.InputUtils.readString(sc, "Giver: ");






                    boolean mon = ui.InputUtils.confirm(sc, "Is it monetary?");
                    income = new GiftIncome(amt, date, giver, mon, "gift");
                    break;
                case 3:
                    String c = ui.InputUtils.readString(sc, "Origin country: ");
                    income = new ForeignIncome(amt, date, c, "foreign");
                    break;
                case 4:
                    BigDecimal purchase = ui.InputUtils.readMoney(sc, "Purchase price: ");

                    BigDecimal sale = amt;

                    Property prop = new Property();


                    prop.setPurchasePrice(purchase);


                    prop.setEstimatedValue(sale);



                    prop.setDescription("Sold property");


                    p.addProperty(prop);

                    income = new PropertySaleIncome(prop, sale, date, "sale");


                    break;

                case 5:
                    String ip = ui.InputUtils.readString(sc, "Intellectual Property (Book/Patent): ");
                    income = new RoyaltyIncome(amt, date, ip, "royalty");
                    break;

                default:


                    System.out.println("Unknown type selected. Operation cancelled.");


                    return;
            }




            if (income != null) {
                svc.addIncomeToPerson(p, income);


                System.out.println("Income added successfully for " + p.getFullName());
            }

        } catch (Exception ex) {



            System.out.println("Unexpected error: " + ex.getMessage());


        }
    }
    @Override
    public String getDesc() {
        return "Enter Income";
    }

}