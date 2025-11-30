package service;

import model.TaxPayment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaxSearchService {
    public List<TaxPayment> findTaxesByRange(List<TaxPayment> taxes, BigDecimal min, BigDecimal max) {
        List<TaxPayment> res = new ArrayList<>();


        for (TaxPayment t : taxes) {


            if ((min == null || t.getAmount().compareTo(min) >= 0) && (max == null || t.getAmount().compareTo(max) <= 0))
                res.add(t);
        }


        res.sort(Comparator.comparing(TaxPayment::getAmount));
        return res;
    }
}
