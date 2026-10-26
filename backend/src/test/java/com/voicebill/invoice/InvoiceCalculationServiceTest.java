package com.voicebill.invoice;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceCalculationServiceTest {
    private final InvoiceCalculationService calc = new InvoiceCalculationService();

    @org.junit.jupiter.api.Test
    void lineTotal_basic() {
        if (!calc.lineTotal(new BigDecimal("2"), new BigDecimal("50")).equals(new BigDecimal("100.00")))
            throw new AssertionError("lineTotal_basic failed");
    }

    @org.junit.jupiter.api.Test
    void taxAmount_basic() {
        if (!calc.taxAmount(new BigDecimal("100"), new BigDecimal("9")).equals(new BigDecimal("9.00")))
            throw new AssertionError("taxAmount_basic failed");
    }

    @org.junit.jupiter.api.Test
    void lineTotal_rounding() {
        if (!calc.lineTotal(new BigDecimal("3"), new BigDecimal("3.333")).equals(new BigDecimal("10.00")))
            throw new AssertionError("lineTotal_rounding failed");
    }
}
