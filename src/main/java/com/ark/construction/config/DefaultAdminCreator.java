package com.ark.construction.config;

import com.ark.construction.entity.AppUser;
import com.ark.construction.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminCreator implements CommandLineRunner {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public DefaultAdminCreator(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        if (repo.count() == 0) {
            AppUser user = new AppUser();
            user.setName("Admin");
            user.setUsername("admin");
            user.setPassword(encoder.encode("admin123"));
            user.setRole("ADMIN");
            user.setActive(true);

            repo.save(user);
        }
    }
}