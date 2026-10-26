package com.voicebill.voice;

import com.voicebill.product.Product;
import com.voicebill.product.ProductRepository;
import com.voicebill.customer.Customer;
import com.voicebill.customer.CustomerRepository;
import com.voicebill.security.CurrentUserResolver;
import com.voicebill.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoiceService {
    private final ProductRepository products;
    private final CustomerRepository customers;
    private final CurrentUserResolver resolver;

    public VoiceService(ProductRepository products, CustomerRepository customers, CurrentUserResolver resolver) {
        this.products = products; this.customers = customers; this.resolver = resolver;
    }

    public VoiceDraft interpret(String transcript) {
        if (transcript == null || transcript.isBlank()) throw new IllegalArgumentException("Empty transcript");
        if (transcript.length() > 500) throw new IllegalArgumentException("Transcript too long");
        User u = resolver.resolve();
        List<Product> userProducts = products.findByUserId(u.getId());
        List<Customer> userCustomers = customers.findByUserId(u.getId());

        // customer: word after "for"
        String customerName = null;
        Matcher cm = Pattern.compile("for\\s+(\\w+)", Pattern.CASE_INSENSITIVE).matcher(transcript);
        if (cm.find()) customerName = cm.group(1);

        Long customerId = null;
        for (Customer c : userCustomers) {
            if (customerName != null && c.getName().equalsIgnoreCase(customerName)) { customerId = c.getId(); break; }
        }

        // items: number + product word
        List<VoiceDraft.Item> items = new ArrayList<>();
        Matcher im = Pattern.compile("(\\d+)\\s+(\\w+)", Pattern.CASE_INSENSITIVE).matcher(transcript);
        while (im.find()) {
            int qty = Integer.parseInt(im.group(1));
            String pname = im.group(2).toLowerCase();
            for (Product p : userProducts) {
                if (p.getName().toLowerCase().contains(pname) || pname.contains(p.getName().toLowerCase())) {
                    items.add(new VoiceDraft.Item(p.getId(), p.getName(), BigDecimal.valueOf(qty), p.getUnit(), p.getPrice()));
                    break;
                }
            }
        }

        String msg = (customerId == null && customerName != null) ? "Customer not matched" : "OK";
        return new VoiceDraft(customerId, customerName, items, msg);
    }
}
