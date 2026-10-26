package com.voicebill.product;

import com.voicebill.security.CurrentUserResolver;
import com.voicebill.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repo;
    private final CurrentUserResolver resolver;

    public ProductController(ProductRepository repo, CurrentUserResolver resolver) {
        this.repo = repo; this.resolver = resolver;
    }

    @GetMapping
    public List<Product> list() {
        User u = resolver.resolve();
        return repo.findByUserId(u.getId());
    }

    @PostMapping
    public Product create(@RequestBody Product p) {
        User u = resolver.resolve();
        p.setUserId(u.getId());
        return repo.save(p);
    }
}
