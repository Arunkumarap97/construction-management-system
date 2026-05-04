package com.ark.construction.service;

import com.ark.construction.entity.PaymentRequest;
import com.ark.construction.repository.BankAccountRepository;
import com.ark.construction.repository.PaymentRequestRepository;
import com.ark.construction.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentRequestService {
    private final PaymentRequestRepository repo;
    private final ProjectRepository projectRepo;
    private final BankAccountRepository bankRepo;

    public PaymentRequestService(PaymentRequestRepository repo,
                                 ProjectRepository projectRepo,
                                 BankAccountRepository bankRepo) {
        this.repo = repo;
        this.projectRepo = projectRepo;
        this.bankRepo = bankRepo;
    }

    public PaymentRequest save(PaymentRequest form) {

        PaymentRequest pr = new PaymentRequest();

        pr.setAmount(form.getAmount());
        pr.setRemarks(form.getRemarks());
        pr.setRequestDate(LocalDate.now());

        if (form.getProjectId() != null) {
            pr.setProject(projectRepo.findById(form.getProjectId()).orElse(null));
        }

        if (form.getBankAccountId() != null) {
            pr.setBankAccount(bankRepo.findById(form.getBankAccountId()).orElse(null));
        }

        return repo.save(pr);
    }

    public PaymentRequest get(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
