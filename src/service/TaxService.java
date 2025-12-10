package service;


import util.AppLogger;
import java.util.logging.Level;


import model.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;



public class TaxService {
    private List<Person> persons = new ArrayList<>();
    private StorageManager storage = new StorageManager();
    private TaxCalculator calculator = new TaxCalculator();

    private boolean autoSaveEnabled = false;
    private Path autoSaveBatchPath = null;
    private Path defaultSavePath = Paths.get("data.ser");

    private static TaxService instance = new TaxService();
    public static TaxService getInstance(){ return instance; }
    private TaxService(){}

    public List<Person> getAllPersons(){ return persons; }

    public Person createPerson(String firstName, String lastName){
        Person p = new Person(firstName, lastName);
        persons.add(p);
        AppLogger.LOGGER.info("Created person: " + p.getFullName());
        if (autoSaveEnabled) {
            saveDefault();
            appendToBatch(String.format("ADD_PERSON|%s|%s", safe(firstName), safe(lastName)));
        }
        return p;
    }

    public void addPerson(Person p){
        if (p == null) {
            AppLogger.LOGGER.warning("Attempted to add null person");
            return;
        }
        persons.add(p);
        AppLogger.LOGGER.info("Added person: " + p.getFullName());
        if (autoSaveEnabled) {
            saveDefault();
            appendToBatch(String.format("ADD_PERSON|%s|%s", safe(p.getFirstName()), safe(p.getLastName())));
        }
    }

    public void addIncomeToPerson(Person person, Income income){
        if (person == null || income == null) return;
        person.addIncome(income);
        AppLogger.LOGGER.info("Added income to " + person.getFullName() + ": " + income.getAmount());
        saveAndLogIncome(person, income);
    }



    private void saveAndLogIncome(Person person, Income income) {
        if (autoSaveEnabled) {
            saveDefault();


            String fullname = safe(person.getFullName());


            String date = income.getDate() != null ? income.getDate().toString() : "";


            String amount = income.getAmount() != null ? income.getAmount().toPlainString() : "0";


            String line;
            if (income instanceof JobIncome) {


                String emp = ((JobIncome) income).getEmployer();



                line = String.format("ADD_INCOME|%s|JOB|%s|%s|%s", fullname, amount, date, safe(emp));
            } else if (income instanceof GiftIncome) {


                GiftIncome g = (GiftIncome) income;


                line = String.format("ADD_INCOME|%s|GIFT|%s|%s|%s|%s", fullname, amount, date, safe(g.getGiver()), String.valueOf(g.isMonetary()));
            } else if (income instanceof ForeignIncome) {

                ForeignIncome f = (ForeignIncome) income;


                line = String.format("ADD_INCOME|%s|FOREIGN|%s|%s|%s", fullname, amount, date, safe(f.getOriginCountry()));
            } else if (income instanceof PropertySaleIncome) {



                PropertySaleIncome ps = (PropertySaleIncome) income;
                String purchase = ps.getPropertySold() != null && ps.getPropertySold().getPurchasePrice() != null ? ps.getPropertySold().getPurchasePrice().toPlainString() : "0";
                String sale = ps.getSalePrice() != null ? ps.getSalePrice().toPlainString() : amount;
                line = String.format("ADD_INCOME|%s|PROPERTYSALE|%s|%s|%s|%s", fullname, amount, date, purchase, sale);
            } else {


                line = String.format("ADD_INCOME|%s|OTHER|%s|%s", fullname, amount, date);
            }
            appendToBatch(line);



        }
    }

    public void addBenefitToPerson(Person person, TaxBenefit benefit){
        if (person == null || benefit == null) return;


        person.addBenefit(benefit);
        if (autoSaveEnabled) {
            saveDefault();


            String line = String.format("ADD_BENEFIT|%s|%s|%s|%s|%d|%d",


                    safe(person.getFullName()),



                    safe(benefit.getDescription()),



                    benefit.getAmount() == null ? "0" : benefit.getAmount().toPlainString(),
                    String.valueOf(benefit.isPercent()),
                    benefit.getValidFromYear(), benefit.getValidToYear());
            appendToBatch(line);
        }
    }




    public void save(String filePath){
        try {
            storage.saveToFile(persons, filePath);
            if (autoSaveBatchPath != null) appendToBatch("SAVE|" + safe(filePath));


            AppLogger.LOGGER.info("Data saved to: " + filePath);


        } catch (Exception e) {


            AppLogger.LOGGER.log(java.util.logging.Level.SEVERE, "Failed to save data to " + filePath, e);
        }
    }

    public void saveDefault(){
        save(defaultSavePath.toString());
    }

    public void load(String filePath){


        try {
            List<Person> loaded = storage.loadFromFile(filePath);


            if (loaded != null) {


                persons.clear();


                persons.addAll(loaded);


                AppLogger.LOGGER.info("Data loaded from: " + filePath);
            }
        } catch (Exception e) {



            AppLogger.LOGGER.log(java.util.logging.Level.SEVERE, "Failed to load data from " + filePath, e);
        }
    }
    public Person findPersonByFullName(String fullname){
        if (fullname == null) return null;



        for (Person p : persons) if (p.getFullName().equalsIgnoreCase(fullname.trim())) return p;
        return null;


    }

    public TaxCalculator getCalculator(){ return calculator; }

    public void setAutoSaveBatch(String batchFilePath, boolean enable){


        if (batchFilePath == null || batchFilePath.isEmpty()) { this.autoSaveBatchPath = null; this.autoSaveEnabled = false; return; }
        this.autoSaveBatchPath = Paths.get(batchFilePath);


        this.autoSaveEnabled = enable;
        if (enable) {


            try { if (autoSaveBatchPath.getParent() != null) Files.createDirectories(autoSaveBatchPath.getParent()); }
            catch (Exception ignored){}



            try { Files.writeString(autoSaveBatchPath, "# Batch script generated by TaxSystem\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND); }
            catch (Exception e) { System.out.println("Cannot create batch file: " + e.getMessage()); }


        }
    }

    private void appendToBatch(String line){
        if (autoSaveBatchPath == null) return;
        try {


            Files.writeString(autoSaveBatchPath, line + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);



        } catch (IOException e) {


            System.out.println("Failed to append to batch file: " + e.getMessage());
        }
    }

    public void processBatchFile(String path){



        BatchProcessor processor = new BatchProcessor(this);



        processor.processFile(path);
    }

    private static String safe(String s) { return s == null ? "" : s.replace("|", "/"); }

    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }

    public String getBatchLogPath() {
        return autoSaveBatchPath == null ? null : autoSaveBatchPath.toString();
    }
}
