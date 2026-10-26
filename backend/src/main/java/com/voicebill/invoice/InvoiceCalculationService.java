package com.voicebill.invoice;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InvoiceCalculationService {
    public BigDecimal lineTotal(BigDecimal qty, BigDecimal price) {
        return qty.multiply(price).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal taxAmount(BigDecimal base, BigDecimal rate) {
        return base.multiply(rate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
