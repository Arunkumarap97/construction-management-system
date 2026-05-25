package com.ark.construction.service;

import com.ark.construction.entity.Expense;
import com.ark.construction.entity.Project;
import com.ark.construction.repository.ExpenseRepository;
import com.ark.construction.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final ProjectRepository projectRepo;

    public ExpenseService(ExpenseRepository expenseRepo,
                          ProjectRepository projectRepo) {
        this.expenseRepo = expenseRepo;
        this.projectRepo = projectRepo;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepo.findByActiveTrueOrderByExpenseDateDesc();
    }

    public Double totalExpenseAll() {
        Double total = expenseRepo.sumAllExpenses();
        return total != null ? total : 0.0;
    }

    public Expense getExpenseById(Long id) {
        return expenseRepo.findById(id).orElseThrow();
    }

    public void saveExpense(Expense formExpense) {

        Expense expense;

        // ✅ EDIT CASE
        if (formExpense.getId() != null) {
            expense = expenseRepo.findById(formExpense.getId()).orElse(new Expense());
        } else {
            // ✅ NEW CASE
            expense = new Expense();
        }
        if (formExpense.getExpenseDate() == null) {
            expense.setExpenseDate(LocalDate.now());
        } else {
            expense.setExpenseDate(formExpense.getExpenseDate());
        }
        expense.setCategory(formExpense.getCategory());
        expense.setVendorName(formExpense.getVendorName());
        expense.setAmount(formExpense.getAmount());
        expense.setNote(formExpense.getNote());

        // project mapping
        if (formExpense.getProjectId() != null) {
            Project project = projectRepo.findById(formExpense.getProjectId()).orElse(null);
            expense.setProject(project);
        } else {
            expense.setProject(null);
        }

        expenseRepo.save(expense);
    }

    public void deleteExpense(Long id) {
        Expense expense = getExpenseById(id);
        expense.setActive(false);
        expenseRepo.save(expense);
    }
    //

    public Double getTotalExpenseByProject(Long projectId) {
        Double total = expenseRepo.getTotalExpenseByProject(projectId);
        return total != null ? total : 0.0;
    }

    public List<Expense> getExpensesByProject(Long projectId) {
        return expenseRepo.findByProject_IdAndActiveTrueOrderByExpenseDateDesc(projectId);
    }
}