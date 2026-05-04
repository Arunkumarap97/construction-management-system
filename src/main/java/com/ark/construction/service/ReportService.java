package com.ark.construction.service;

import com.ark.construction.dto.ProjectReportDto;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ExpenseRepository;
import com.ark.construction.repository.PaymentRepository;
import com.ark.construction.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final ProjectRepository projectRepo;
    private final PaymentRepository paymentRepo;
    private final ExpenseRepository expenseRepo;

    public ReportService(ProjectRepository projectRepo,
                         PaymentRepository paymentRepo,
                         ExpenseRepository expenseRepo) {
        this.projectRepo = projectRepo;
        this.paymentRepo = paymentRepo;
        this.expenseRepo = expenseRepo;
    }

    public List<ProjectReportDto> projectReports() {

        List<ProjectReportDto> reports = new ArrayList<>();

        for (Project p : projectRepo.findAll()) {

            Double paid = paymentRepo.getTotalPaidByProject(p.getId());
            Double expense = expenseRepo.getTotalExpenseByProject(p.getId());

            reports.add(new ProjectReportDto(
                    p.getId(),
                    p.getProjectName(),
                    p.getClient() != null ? p.getClient().getName() : "-",
                    p.getTotalCost(),
                    paid,
                    expense
            ));
        }

        return reports;
    }

    public List<ProjectReportDto> projectReportsByDate(String from, String to) {

        if (from == null || to == null || from.equals("null") || to.equals("null")) {
            return projectReports(); // fallback
        }
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);

        List<ProjectReportDto> reports = new ArrayList<>();

        for (Project p : projectRepo.findAll()) {

            Double paid = paymentRepo.sumByProjectAndDate(p.getId(), start, end);
            Double expense = expenseRepo.sumByProjectAndDate(p.getId(), start, end);

            reports.add(new ProjectReportDto(
                    p.getId(),
                    p.getProjectName(),
                    p.getClient() != null ? p.getClient().getName() : "-",
                    p.getTotalCost(),
                    paid,
                    expense
            ));
        }

        return reports;
    }
}