package com.ark.construction.service;

import com.ark.construction.entity.Payment;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.PaymentRepository;
import com.ark.construction.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;
    private final ProjectRepository projectRepo;

    public PaymentService(PaymentRepository paymentRepo,
                          ProjectRepository projectRepo) {
        this.paymentRepo = paymentRepo;
        this.projectRepo = projectRepo;
    }

    public List<Payment> getPaymentsByProject(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        return project.getPayments();
    }

    public Double getTotalPaid(Long projectId) {
        return paymentRepo.getTotalPaidByProject(projectId);
    }

    public String addPayment(Long projectId, Payment payment) {

        Project project = projectRepo.findById(projectId).orElseThrow();

        Double totalPaid = getTotalPaid(projectId);
        Double pending = project.getTotalCost() - totalPaid;

        // ❌ validation
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            return "invalid_amount";
        }

        if (payment.getAmount() > pending) {
            return "overpayment";
        }

        // ✅ default date
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        payment.setProject(project);

        paymentRepo.save(payment);

        return "success";
    }
}