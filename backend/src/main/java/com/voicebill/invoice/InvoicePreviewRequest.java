package com.voicebill.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoicePreviewRequest(Long customerId, LocalDate invoiceDate, LocalDate dueDate, String notes, List<Line> items) {
    public record Line(Long productId, BigDecimal quantity) {}
}
