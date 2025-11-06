package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import service.TaxCalculator;

public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private List<Income> incomes = new ArrayList<>();
    private List<Dependent> dependents = new ArrayList<>();
    private List<Property> properties = new ArrayList<>();
    private List<TaxBenefit> benefits = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();
    private List<TaxPayment> taxPayments = new ArrayList<>();

    public void addIncome(Income income) {
        /* заглушка */
    }

    public boolean removeIncome(String incomeId) {
        /* заглушка */
        return false;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void addDependent(Dependent d) {
        /* заглушка */

    }

    public boolean removeDependent(String dependentId) {
        /* заглушка */

        return false;
    }

    public List<Dependent> getDependents() {
        return dependents;
    }

    public void addProperty(Property p) {
        /* заглушка */

    }

    public boolean removeProperty(String propertyId) {
        /* заглушка */

        return false;
    }

    public void addBenefit(TaxBenefit benefit) {
        /* заглушка */

    }

    public void removeBenefit(String benefitId) {
        /* заглушка */

    }

    public BigDecimal getTotalAnnualIncome(int year) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    public BigDecimal getTaxableIncome(int year) {
        /* заглушка */

        return BigDecimal.ZERO;
    }

    public List<TaxPayment> calculateTaxes(int year, TaxCalculator calculator) {
        /* заглушка */

        return new ArrayList<>();
    }

    public void addDocument(Document doc) {
        /* заглушка */

    }

    public List<Document> getDocuments() {
        /* заглушка */

        return documents;
    }

    @Override
    public String toString() {
        /* заглушка */




        return String.format("Person[id=%s, name=%s %s]", id, firstName, lastName);
    }






}
