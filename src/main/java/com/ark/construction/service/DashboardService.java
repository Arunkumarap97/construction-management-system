package com.ark.construction.service;

import com.ark.construction.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final ProjectRepository projectRepo;
    private final PaymentRepository paymentRepo;
    private final ExpenseRepository expenseRepo;
    private final ClientRepository clientRepo;

    public DashboardService(ProjectRepository projectRepo,
                            PaymentRepository paymentRepo,
                            ExpenseRepository expenseRepo,
                            ClientRepository clientRepo) {
        this.projectRepo = projectRepo;
        this.paymentRepo = paymentRepo;
        this.expenseRepo = expenseRepo;
        this.clientRepo = clientRepo;
    }

    public Long totalClients() {
        return clientRepo.count();
    }

    public Long activeClients() {
        return clientRepo.countByActiveTrue();
    }

    public Long totalProjects() {
        return projectRepo.count();
    }

    public Long activeProjects() {
        return projectRepo.countByStatus("ONGOING");
    }

    public Double totalRevenue() {
        return paymentRepo.sumAllPayments();
    }

    public Double totalExpense() {
        return expenseRepo.sumAllExpenses();
    }

    public Double pendingAmount() {
        return totalRevenue() - totalExpense();
    }
}