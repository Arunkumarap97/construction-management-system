package com.ark.construction.service;

import com.ark.construction.entity.BankAccount;
import com.ark.construction.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    private final BankAccountRepository repo;

    public BankAccountService(BankAccountRepository repo) {
        this.repo = repo;
    }

    public List<BankAccount> getAll() {
        return repo.findAll();
    }

    public List<BankAccount> getActive() {
        return repo.findByActiveTrue();
    }

    public BankAccount save(BankAccount bank) {

        // 🔥 only one default allowed
        if (Boolean.TRUE.equals(bank.getIsDefault())) {
            repo.findByIsDefaultTrue().ifPresent(b -> {
                b.setIsDefault(false);
                repo.save(b);
            });
        }

        return repo.save(bank);
    }

    public BankAccount get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
