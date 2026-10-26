package com.voicebill.invoice;

import com.voicebill.product.Product;
import com.voicebill.product.ProductRepository;
import com.voicebill.customer.Customer;
import com.voicebill.customer.CustomerRepository;
import com.voicebill.security.CurrentUserResolver;
import com.voicebill.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoices;
    private final ProductRepository products;
    private final CustomerRepository customers;
    private final CurrentUserResolver resolver;
    private final InvoiceCalculationService calc;

    public InvoiceService(InvoiceRepository invoices, ProductRepository products, CustomerRepository customers,
                          CurrentUserResolver resolver, InvoiceCalculationService calc) {
        this.invoices = invoices; this.products = products; this.customers = customers;
        this.resolver = resolver; this.calc = calc;
    }

    @Transactional
    public Invoice create(InvoicePreviewRequest req) {
        User u = resolver.resolve();
        Invoice inv = new Invoice();
        inv.setUserId(u.getId());
        inv.setInvoiceNumber("INV-" + System.currentTimeMillis());
        inv.setInvoiceDate(req.invoiceDate() != null ? req.invoiceDate() : LocalDate.now());
        inv.setDueDate(req.dueDate());
        inv.setNotes(req.notes());
        if (req.customerId() != null) {
            Customer c = customers.findById(req.customerId()).orElseThrow();
            if (!c.getUserId().equals(u.getId())) throw new IllegalArgumentException("Customer not found");
            inv.setCustomerId(c.getId());
            inv.setCustomerName(c.getName());
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        for (InvoicePreviewRequest.Line l : req.items()) {
            Product p = products.findById(l.productId()).orElseThrow();
            if (!p.getUserId().equals(u.getId())) throw new IllegalArgumentException("Product not found");
            if (!p.isActive()) throw new IllegalArgumentException("Product inactive");
            if (l.quantity() == null || l.quantity().compareTo(BigDecimal.ZERO) <= 0)
                throw new IllegalArgumentException("Invalid quantity");
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(inv);
            item.setProductId(p.getId());
            item.setDescription(p.getName());
            item.setQuantity(l.quantity());
            item.setUnit(p.getUnit());
            item.setUnitPrice(p.getPrice());
            BigDecimal lt = calc.lineTotal(l.quantity(), p.getPrice());
            item.setLineTotal(lt);
            inv.getItems().add(item);
            subtotal = subtotal.add(lt);
        }
        inv.setSubtotal(subtotal);
        inv.setTaxTotal(taxTotal);
        inv.setTotal(subtotal.add(taxTotal));
        inv.setStatus("FINALIZED");
        return invoices.save(inv);
    }

    public List<Invoice> list() {
        User u = resolver.resolve();
        return invoices.findByUserId(u.getId());
    }

    public Invoice get(Long id) {
        User u = resolver.resolve();
        Invoice inv = invoices.findById(id).orElseThrow();
        if (!inv.getUserId().equals(u.getId())) throw new IllegalArgumentException("Not found");
        return inv;
    }
}
