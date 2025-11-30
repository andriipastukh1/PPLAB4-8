package model;

import java.io.Serializable;


import java.time.LocalDate;


import java.time.Period;

public class Dependent implements Serializable {



    private String id = java.util.UUID.randomUUID().toString();
    private String name;
    private LocalDate birthDate;

    public Dependent() {}
    public Dependent(String name, LocalDate birthDate) {

        this.name = name;

        this.birthDate = birthDate;


    }

    public String getName() { return name;


    }
    public LocalDate getBirthDate() { return birthDate;

    }



    public int getAge(LocalDate onDate) {
        if (birthDate == null || onDate == null) return 0;



        return Period.between(birthDate, onDate).getYears();


    }


    public boolean qualifiesForBenefit(int year) {
        LocalDate cutoff = LocalDate.of(year, 12, 31);
        return getAge(cutoff) <= 18;


    }
}
