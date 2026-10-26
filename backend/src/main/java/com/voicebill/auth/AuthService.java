package com.voicebill.auth;

import com.voicebill.security.JwtService;
import com.voicebill.user.User;
import com.voicebill.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    public User register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) throw new IllegalArgumentException("Email already registered");
        User u = new User();
        u.setFullName(req.fullName());
        u.setEmail(req.email());
        u.setPasswordHash(encoder.encode(req.password()));
        return users.save(u);
    }

    public String login(LoginRequest req) {
        User u = users.findByEmail(req.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(req.password(), u.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");
        return jwt.generate(u.getEmail());
    }
}
