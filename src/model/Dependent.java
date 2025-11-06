package model;

import java.time.LocalDate;
import java.time.Period;

public class Dependent {
    private String id;
    private String name;
    private LocalDate birthDate;

    public int getAge(LocalDate onDate) {
        return Period.between(birthDate, onDate).getYears();
    }





    public boolean qualifiesForBenefit(int year) {
        /* заглушка */



        return false;
    }

    public String getName() { return name; }
}
