package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxPayment {
    private String id;
    private LocalDate dateCalculated;
    private BigDecimal taxAmount;
    private int taxYear;
    private String reason;
    private boolean paid;
    private TaxCategory TaxCategory_name;


    public void markAsPaid(LocalDate paidDate) {
        /* заглушка */

    }

    public boolean isPaid() { return paid; }
    public BigDecimal getAmount() { return taxAmount; }
    public String getReason() { return reason; }
}
