package com.voicebill.security;

import com.voicebill.user.User;
import com.voicebill.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserResolver {
    private final UserRepository users;

    public CurrentUserResolver(UserRepository users) { this.users = users; }

    public User resolve() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) throw new IllegalArgumentException("Not authenticated");
        return users.findByEmail(auth.getName()).orElseThrow();
    }
}
