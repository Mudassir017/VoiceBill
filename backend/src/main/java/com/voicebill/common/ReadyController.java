package com.voicebill.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@RestController
public class ReadyController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/api/ready")
    public Map<String, Object> ready() {
        try (Connection c = dataSource.getConnection()) {
            boolean ok = c.isValid(2);
            return Map.of("status", ok ? "ready" : "db-unavailable", "database", ok);
        } catch (Exception e) {
            return Map.of("status", "db-unavailable", "database", false);
        }
    }
}
