package service;

import model.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class BatchProcessor {
    private final TaxService svc;

    public BatchProcessor(TaxService svc){
        this.svc = svc;




    }

    public void processFile(String path){
        try {
            List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            for (String raw : lines) {


                String line = raw.trim();


                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");


                String cmd = parts[0].toUpperCase();
                switch (cmd) {
                    case "ADD_PERSON":
                        if (parts.length >= 3) svc.createPerson(parts[1].trim(), parts[2].trim());
                        break;
                    case "ADD_INCOME":

                        if (parts.length >= 5) {


                            String fullname = parts[1].trim();



                            String type = parts[2].trim().toUpperCase();

                            BigDecimal amount = new BigDecimal(parts[3].trim());
                            LocalDate date = parseDateSafe(parts[4].trim());


                            model.Person p = svc.findPersonByFullName(fullname);


                            if (p == null) p = svc.createPerson(fullname.split(" ",2)[0], fullname.split(" ",2).length>1?fullname.split(" ",2)[1]:"");
                            switch (type) {
                                case "JOB":




                                    String emp = parts.length>=6 ? parts[5] : "";

                                    JobIncome ji = new JobIncome(amount, date, emp, "job (batch)");

                                    svc.addIncomeToPerson(p, ji);
                                    break;


                                case "GIFT":




                                    String giver = parts.length>=6 ? parts[5] : "";


                                    boolean mon = parts.length>=7 ? Boolean.parseBoolean(parts[6]) : true;


                                    GiftIncome gi = new GiftIncome(amount, date, giver, mon, "gift (batch)");


                                    svc.addIncomeToPerson(p, gi);
                                    break;


                                case "FOREIGN":




                                    String country = parts.length>=6 ? parts[5] : "";

                                    ForeignIncome fi = new ForeignIncome(amount, date, country, "foreign (batch)");

                                    svc.addIncomeToPerson(p, fi);



                                    break;


                                case "PROPERTYSALE":

                                    BigDecimal purchase = parts.length>=6 ? new BigDecimal(parts[5]) : BigDecimal.ZERO;


                                    BigDecimal sale = parts.length>=7 ? new BigDecimal(parts[6]) : amount;
                                    Property prop = new Property();




                                    prop.setPurchasePrice(purchase);
                                    prop.setEstimatedValue(sale);


                                    prop.setDescription("Property batch");


                                    PropertySaleIncome psi = new PropertySaleIncome(prop, sale, date, "sale (batch)");
                                    p.addProperty(prop);


                                    svc.addIncomeToPerson(p, psi);
                                    break;
                                default:


                                    System.out.println("Unknown income type in batch: " + type);
                            }
                        }
                        break;
                    case "ADD_BENEFIT":
                        if (parts.length >= 7) {
                            String fullname = parts[1].trim();
                            String name = parts[2].trim();


                            BigDecimal amt = new BigDecimal(parts[3].trim());


                            boolean isPercent = Boolean.parseBoolean(parts[4].trim());


                            int from = Integer.parseInt(parts[5].trim());
                            int to = Integer.parseInt(parts[6].trim());
                            Person p = svc.findPersonByFullName(fullname);


                            if (p == null) p = svc.createPerson(fullname.split(" ",2)[0], fullname.split(" ",2).length>1?fullname.split(" ",2)[1]:"");
                            TaxBenefit b = new TaxBenefit(name, amt, isPercent, from, to);

                            svc.addBenefitToPerson(p, b);
                        }
                        break;
                    case "SAVE":
                        if (parts.length >= 2) svc.save(parts[1].trim());


                        else svc.saveDefault();


                        break;
                    default:
                        System.out.println("Unknown batch command: " + cmd);


                }
            }
            System.out.println("Batch processssing finished: " + path);


        } catch (IOException e) {
            System.out.println("Batch file error: " + e.getMessage());


        } catch (Exception e){
            System.out.println("Batch processing exception: " + e.getMessage());


        }
    }

    private LocalDate parseDateSafe(String s){



        try { return s == null || s.isBlank() ? LocalDate.now() : LocalDate.parse(s); }


        catch (Exception e) { return LocalDate.now(); }
    }
}
