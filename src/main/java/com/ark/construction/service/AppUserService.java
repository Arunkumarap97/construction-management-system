package com.ark.construction.service;

import com.ark.construction.entity.AppUser;
import com.ark.construction.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public String changePassword(String username,
                                 String currentPassword,
                                 String newPassword,
                                 String confirmPassword) {

        AppUser user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(currentPassword, user.getPassword())) {
            return "current_invalid";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "password_mismatch";
        }

        if (newPassword.length() < 6) {
            return "weak_password";
        }

        user.setPassword(encoder.encode(newPassword));
        repo.save(user);

        return "success";
    }
}