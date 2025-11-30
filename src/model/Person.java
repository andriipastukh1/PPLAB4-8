package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person implements Serializable {
    private String id = java.util.UUID.randomUUID().toString();
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    // collections
    public List<Income> incomes = new ArrayList<>();
    public List<Dependent> dependents = new ArrayList<>();


    public List<Property> properties = new ArrayList<>();


    public List<TaxBenefit> benefits = new ArrayList<>();
    public List<Document> documents = new ArrayList<>();
    public List<TaxPayment> taxPayments = new ArrayList<>();

    public Person() {}
    public Person(String firstName, String lastName) { this.firstName = firstName; this.lastName = lastName; }

    public String getId() { return id; }


    public String getFirstName() { return firstName; }


    public String getLastName() { return lastName;

    }


    public String getFullName() { return (firstName + " " + (lastName==null?"":lastName)).trim();

    }

    public void addIncome(Income income) { if (income != null) incomes.add(income); }
    public boolean removeIncome(String incomeId) { return incomes.removeIf(i -> i.getId().equals(incomeId)); }

    public void addDependent(Dependent d) {

    if (d != null) dependents.add(d);

    }
    public boolean removeDependent(String name) {

     return dependents.removeIf(d -> d.getName().equalsIgnoreCase(name));

     }

    public void addProperty(Property p) {

     if (p != null) properties.add(p);

     }


    public boolean removeProperty(String propertyId) { return properties.removeIf(p -> p.getId().equals(propertyId)); }

    public void addBenefit(TaxBenefit b) { if (b != null)

     benefits.add(b);

    }
    public void removeBenefit(String benefitName) {

    benefits.removeIf(b -> b.getDescription().equalsIgnoreCase(benefitName));

     }



    public void addTaxPayment(TaxPayment payment) {
        if (payment != null) {
            this.taxPayments.add(payment);
        }
    }

    public List<TaxPayment> getTaxPaymentsForYear(int year) {
        return taxPayments.stream()


                .filter(t -> t.getTaxYear() == year)


                .collect(Collectors.toList());
    }

    public List<TaxPayment> getTaxPaymentsInRange(BigDecimal min, BigDecimal max) {
        return taxPayments.stream()
                .filter(t -> {


                    BigDecimal amt = t.getAmount();


                    return amt != null && amt.compareTo(min) >= 0 && amt.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());


    }



    public BigDecimal getTotalAnnualIncome(int year) {
        BigDecimal sum = BigDecimal.ZERO;


        for (Income inc : incomes) if (inc != null && inc.isAnnual(year) && inc.getAmount()!=null) sum = sum.add(inc.getAmount());
        return sum;
    }

    public BigDecimal getTaxableIncome(int year) {
        BigDecimal base = getTotalAnnualIncome(year);



        for (TaxBenefit b : benefits) if (b != null && b.isApplicable(year)) base = base.subtract(b.applyBenefit(base));
        if (base.compareTo(BigDecimal.ZERO) < 0) base = BigDecimal.ZERO;


        return base;


    }

    @Override


    public String toString() {


        return String.format("Person[id=%s, name=%s %s]", id, firstName, lastName);
    }
}