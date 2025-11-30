package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TaxPayment implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id = UUID.randomUUID().toString();
    private LocalDate dateCalculated = LocalDate.now();
    private BigDecimal taxAmount;
    private int taxYear;
    private String reason;
    private boolean paid;
    private TaxCategory taxCategory;

    public TaxPayment() {}

    public TaxPayment(BigDecimal taxAmount, int taxYear, String reason, TaxCategory taxCategory) {
        this.taxAmount = taxAmount == null ? BigDecimal.ZERO : taxAmount;
        this.taxYear = taxYear;
        this.reason = reason;
        this.taxCategory = taxCategory;
        this.paid = false;
    }

    public void markAsPaid() {
        this.paid = true;
        this.dateCalculated = LocalDate.now();
    }


//    public getDateCalculated() { return dateCalculated; }
    public boolean isPaid() {



    return paid;



     }

    public BigDecimal getAmount() {





    return taxAmount; }

    public String getReason() { return reason; }

    public int getTaxYear() { return taxYear;



     }

    public TaxCategory getTaxCategory() { return taxCategory;



     }

    public LocalDate getDateCalculated() { return dateCalculated; }

    @Override
    public String toString() {


        return String.format("TaxPayment[%s %s amount=%s year=%d paid=%s]", taxCategory, reason, taxAmount, taxYear, paid);
    }
}
