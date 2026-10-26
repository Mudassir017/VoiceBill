package com.voicebill.auth;

import com.voicebill.user.User;
import com.voicebill.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    private final UserRepository users;
    private final com.voicebill.security.JwtService jwt;

    public AuthController(AuthService auth, UserRepository users, com.voicebill.security.JwtService jwt) {
        this.auth = auth; this.users = users; this.jwt = jwt;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest req, HttpServletResponse res) {
        auth.register(req);
        String token = auth.login(new LoginRequest(req.email(), req.password()));
        setCookie(res, token);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req, HttpServletResponse res) {
        setCookie(res, auth.login(req));
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse res) {
        ResponseCookie c = ResponseCookie.from("token", "").path("/").maxAge(0).httpOnly(true).build();
        res.addHeader("Set-Cookie", c.toString());
    }

    @GetMapping("/me")
    public User me(@RequestHeader(value = "Cookie", required = false) String cookie) {
        // simplified: parse token from cookie header
        if (cookie == null) throw new IllegalArgumentException("Not authenticated");
        String token = null;
        for (String part : cookie.split(";")) {
            part = part.trim();
            if (part.startsWith("token=")) token = part.substring(6);
        }
        if (token == null) throw new IllegalArgumentException("Not authenticated");
        String email = jwt.extractEmail(token);
        return users.findByEmail(email).orElseThrow();
    }

    private void setCookie(HttpServletResponse res, String token) {
        ResponseCookie c = ResponseCookie.from("token", token).path("/").httpOnly(true).sameSite("Lax").maxAge(86400).build();
        res.addHeader("Set-Cookie", c.toString());
    }
}
