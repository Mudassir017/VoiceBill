package com.voicebill.customer;

import com.voicebill.security.CurrentUserResolver;
import com.voicebill.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repo;
    private final CurrentUserResolver resolver;

    public CustomerController(CustomerRepository repo, CurrentUserResolver resolver) {
        this.repo = repo; this.resolver = resolver;
    }

    @GetMapping
    public List<Customer> list() {
        User u = resolver.resolve();
        return repo.findByUserId(u.getId());
    }

    @PostMapping
    public Customer create(@RequestBody Customer c) {
        User u = resolver.resolve();
        c.setUserId(u.getId());
        return repo.save(c);
    }
}
