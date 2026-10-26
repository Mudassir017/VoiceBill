package com.voicebill.dashboard;

import com.voicebill.invoice.Invoice;
import com.voicebill.invoice.InvoiceRepository;
import com.voicebill.security.CurrentUserResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final InvoiceRepository invoices;
    private final CurrentUserResolver resolver;

    public DashboardController(InvoiceRepository invoices, CurrentUserResolver resolver) {
        this.invoices = invoices; this.resolver = resolver;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        Long uid = resolver.resolve().getId();
        return Map.of(
            "totalInvoices", invoices.countByUserId(uid),
            "totalAmount", invoices.sumTotalByUserId(uid)
        );
    }

    @GetMapping("/recent")
    public List<Invoice> recent() {
        Long uid = resolver.resolve().getId();
        return invoices.findRecentByUserId(uid, PageRequest.of(0, 5));
    }
}
