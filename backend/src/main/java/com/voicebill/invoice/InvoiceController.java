package com.voicebill.invoice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) { this.service = service; }

    @PostMapping
    public Invoice create(@RequestBody InvoicePreviewRequest req) { return service.create(req); }

    @GetMapping
    public List<Invoice> list() { return service.list(); }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable Long id) { return service.get(id); }
}
