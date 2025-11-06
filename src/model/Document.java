package model;

import java.time.LocalDate;

public class Document {
    private String id;
    private String DocumentType;
    private LocalDate issueDate;
    private String filePath;
    private String note;

    public String getFilePath() { return filePath; }
    public String  getType() { return DocumentType; }

    public boolean isValidForTaxYear(int year) {
        /* заглушка */





        return false;
    }
}
