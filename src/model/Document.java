package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Document implements Serializable {
    private String id = java.util.UUID.randomUUID().toString();
    private String documentType;
    private LocalDate issueDate;


    private String filePath;



    private String note;

    public Document() {}
    public String getFilePath() { return filePath; }


    public String getType() { return documentType; }



    public boolean isValidForTaxYear(int year) {


        return issueDate != null && issueDate.getYear() <= year;
    }
}
