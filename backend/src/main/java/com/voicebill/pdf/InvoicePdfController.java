package com.voicebill.pdf;

import com.voicebill.invoice.Invoice;
import com.voicebill.invoice.InvoiceItem;
import com.voicebill.invoice.InvoiceService;
import com.voicebill.security.CurrentUserResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/invoices")
public class InvoicePdfController {
    private final InvoiceService invoiceService;
    private final CurrentUserResolver resolver;

    public InvoicePdfController(InvoiceService invoiceService, CurrentUserResolver resolver) {
        this.invoiceService = invoiceService; this.resolver = resolver;
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Invoice inv = invoiceService.get(id); // ownership enforced
        String text = "Invoice " + inv.getInvoiceNumber() + "\nCustomer: " + inv.getCustomerName()
                + "\nDate: " + inv.getInvoiceDate() + "\nTotal: " + inv.getTotal() + "\n";
        for (InvoiceItem it : inv.getItems()) {
            text += it.getDescription() + " x " + it.getQuantity() + " = " + it.getLineTotal() + "\n";
        }
        byte[] pdf = simplePdf(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + inv.getInvoiceNumber() + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    private byte[] simplePdf(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("%PDF-1.4\n");
        sb.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
        sb.append("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
        sb.append("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");
        String content = "BT /F1 12 Tf 50 750 Td (" + text.replace("(", "\\(").replace(")", "\\)").replace("\n", ") Tj T* (") + ") Tj ET";
        sb.append("4 0 obj\n<< /Length ").append(content.length()).append(" >>\nstream\n").append(content).append("\nendstream\nendobj\n");
        sb.append("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");
        sb.append("xref\n0 6\n0000000000 65535 f \n");
        sb.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n0\n%%EOF");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
